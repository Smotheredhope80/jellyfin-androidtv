package org.jellyfin.playback.media3.exoplayer

import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.core.content.getSystemService
import androidx.core.graphics.TypefaceCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.extractor.DefaultExtractorsFactory
import androidx.media3.extractor.ts.TsExtractor
import androidx.media3.ui.SubtitleView
import androidx.media3.ui.CaptionStyleCompat
import io.github.peerless2012.ass.media.AssHandler
import io.github.peerless2012.ass.media.factory.AssRenderersFactory
import io.github.peerless2012.ass.media.kt.withAssMkvSupport
import io.github.peerless2012.ass.media.parser.AssSubtitleParserFactory
import io.github.peerless2012.ass.media.type.AssRenderType
import io.github.peerless2012.ass.media.widget.AssSubtitleView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jellyfin.playback.core.backend.BasePlayerBackend
import org.jellyfin.playback.core.mediastream.MediaStream
import org.jellyfin.playback.core.mediastream.PlayableMediaStream
import org.jellyfin.playback.core.mediastream.mediaStream
import org.jellyfin.playback.core.mediastream.mediatype.MediaType
import org.jellyfin.playback.core.mediastream.mediatype.mediaType
import org.jellyfin.playback.core.mediastream.normalizationGain
import org.jellyfin.playback.core.model.PlayState
import org.jellyfin.playback.core.model.PositionInfo
import org.jellyfin.playback.core.queue.QueueEntry
import org.jellyfin.playback.core.support.PlaySupportReport
import org.jellyfin.playback.core.timedevent.TimedEvent
import org.jellyfin.playback.core.ui.PlayerSubtitleView
import org.jellyfin.playback.core.ui.PlayerSurfaceView
import org.jellyfin.playback.media3.exoplayer.support.getPlaySupportReport
import org.jellyfin.playback.media3.exoplayer.support.toFormats
import timber.log.Timber
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@OptIn(UnstableApi::class)
class ExoPlayerBackend(
	private val context: Context,
	private val exoPlayerOptions: ExoPlayerOptions,
) : BasePlayerBackend() {
	companion object {
		const val TS_SEARCH_BYTES_LM = TsExtractor.TS_PACKET_SIZE * 1800
		const val TS_SEARCH_BYTES_HM = TsExtractor.DEFAULT_TIMESTAMP_SEARCH_BYTES
		const val MEDIA_ITEM_COUNT_MAX = 10
	}

	private var currentStream: PlayableMediaStream? = null
	private var subtitleView: SubtitleView? = null
	private var subtitleStyle: SubtitleStyle? = null
	private val audioPipeline = ExoPlayerAudioPipeline()
	private val audioAttributeState = AudioAttributeState()
	private val timedEventState = TimedEventState()
	private var lastKnownDuration: Duration? = null
	private val _audioTracks = MutableStateFlow<List<PlayerTrackOption>>(emptyList())
	private val _subtitleTracks = MutableStateFlow<List<PlayerTrackOption>>(emptyList())
	val audioTracks = _audioTracks.asStateFlow()
	val subtitleTracks = _subtitleTracks.asStateFlow()

	private val assHandler by lazy {
		AssHandler(AssRenderType.OVERLAY_OPEN_GL)
	}

	private val exoPlayer by lazy {
		val dataSourceFactory = DefaultDataSource.Factory(
			context,
			exoPlayerOptions.baseDataSourceFactory,
		)
		val extractorsFactory = DefaultExtractorsFactory().apply {
			val isLowRamDevice = context.getSystemService<ActivityManager>()?.isLowRamDevice == true
			setTsExtractorTimestampSearchBytes(
				when (isLowRamDevice) {
					true -> TS_SEARCH_BYTES_LM
					false -> TS_SEARCH_BYTES_HM
				}
			)
			setConstantBitrateSeekingEnabled(true)
			setConstantBitrateSeekingAlwaysEnabled(true)
		}

		val configuredExtractorsFactory = when {
			exoPlayerOptions.forceDolbyVisionProfile7Hevc -> DolbyVisionProfile7HevcExtractorsFactory(extractorsFactory)
			else -> extractorsFactory
		}

		val mediaSourceFactory = if (exoPlayerOptions.enableLibass) {
			val assSubtitleParserFactory = AssSubtitleParserFactory(assHandler)
			val assExtractorsFactory = configuredExtractorsFactory.withAssMkvSupport(assSubtitleParserFactory, assHandler)
			DefaultMediaSourceFactory(dataSourceFactory, assExtractorsFactory).apply {
				setSubtitleParserFactory(assSubtitleParserFactory)
			}
		} else DefaultMediaSourceFactory(dataSourceFactory, configuredExtractorsFactory)

		val renderersFactory = DefaultRenderersFactory(context).apply {
			setEnableDecoderFallback(true)
			setExtensionRendererMode(
				when (exoPlayerOptions.preferFfmpeg) {
					true -> DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
					false -> DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON
				}
			)
		}.let { renderersFactory ->
			if (exoPlayerOptions.enableLibass) AssRenderersFactory(assHandler, renderersFactory)
			else renderersFactory
		}

		val loadControl = DefaultLoadControl.Builder()
			.setBufferDurationsMs(
				exoPlayerOptions.minBufferDuration?.inWholeMilliseconds?.toInt() ?: DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
				exoPlayerOptions.maxBufferDuration?.inWholeMilliseconds?.toInt() ?: DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
				exoPlayerOptions.bufferForPlaybackDuration?.inWholeMilliseconds?.toInt() ?: DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
				exoPlayerOptions.bufferForPlaybackAfterRebufferDuration?.inWholeMilliseconds?.toInt() ?: DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS,
			)
			.build()

		ExoPlayer.Builder(context)
			.setLoadControl(loadControl)
			.setRenderersFactory(renderersFactory)
			.setTrackSelector(DefaultTrackSelector(context).apply {
				setParameters(buildUponParameters().apply {
					if (exoPlayerOptions.forceDolbyVisionProfile7Hevc) {
						// Some DV7 remuxes have an incorrectly flagged non-English default track.
						// Prefer English before preparation so playback cannot stall before the
						// user has a chance to open the audio selector.
						setPreferredAudioLanguage("eng")
					}
					setAudioOffloadPreferences(
						TrackSelectionParameters.AudioOffloadPreferences.DEFAULT.buildUpon().apply {
							setAudioOffloadMode(TrackSelectionParameters.AudioOffloadPreferences.AUDIO_OFFLOAD_MODE_ENABLED)
						}.build()
					)
					setAllowInvalidateSelectionsOnRendererCapabilitiesChange(true)
				})
			})
			.setMediaSourceFactory(mediaSourceFactory)
			.setPauseAtEndOfMediaItems(true)
			.build()
			.also { player ->
				player.addListener(PlayerListener())

				if (exoPlayerOptions.enableDebugLogging) {
					player.addAnalyticsListener(EventLogger())
				}

				if (exoPlayerOptions.enableLibass) {
					assHandler.init(player)
				}
			}
	}

	inner class PlayerListener : Player.Listener {
		override fun onIsPlayingChanged(isPlaying: Boolean) {
			val state = when {
				isPlaying -> PlayState.PLAYING
				exoPlayer.playbackState == Player.STATE_IDLE || exoPlayer.playbackState == Player.STATE_ENDED -> PlayState.STOPPED
				else -> PlayState.PAUSED
			}
			listener?.onPlayStateChange(state)
		}

		override fun onPlayerError(error: PlaybackException) {
			listener?.onPlayStateChange(PlayState.ERROR)
		}

		override fun onVideoSizeChanged(size: VideoSize) {
			if (size != VideoSize.UNKNOWN) {
				listener?.onVideoSizeChange(size.width, size.height)
			}
		}

		override fun onCues(cueGroup: CueGroup) {
			subtitleView?.setCues(cueGroup.cues)
		}

		override fun onPlaybackStateChanged(playbackState: Int) {
			onIsPlayingChanged(exoPlayer.isPlaying)
		}

		override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
			if (reason == Player.PLAY_WHEN_READY_CHANGE_REASON_END_OF_MEDIA_ITEM) {
				listener?.onMediaStreamEnd(requireNotNull(currentStream))
			}
		}

		override fun onAudioSessionIdChanged(audioSessionId: Int) {
			audioPipeline.setAudioSessionId(audioSessionId)
		}

		override fun onTracksChanged(tracks: Tracks) {
			_audioTracks.value = tracks.optionsForType(C.TRACK_TYPE_AUDIO)
			_subtitleTracks.value = tracks.optionsForType(C.TRACK_TYPE_TEXT)
		}

		override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
			val queueEntry = mediaItem?.localConfiguration?.tag as? QueueEntry
			audioPipeline.normalizationGain = queueEntry?.normalizationGain
		}

		override fun onTimelineChanged(timeline: Timeline, reason: Int) {
			val duration = exoPlayer.duration.takeUnless { it == C.TIME_UNSET }?.milliseconds
			if (duration == lastKnownDuration) return
			timedEventState.onDurationChange(exoPlayer, duration)
			lastKnownDuration = duration
		}

		override fun onPositionDiscontinuity(oldPosition: Player.PositionInfo, newPosition: Player.PositionInfo, reason: Int) {
			timedEventState.onSeek(oldPosition.positionMs.milliseconds, newPosition.positionMs.milliseconds, lastKnownDuration ?: Duration.ZERO)
		}
	}

	override fun supportsStream(
		stream: MediaStream
	): PlaySupportReport = exoPlayer.getPlaySupportReport(stream.toFormats())

	override fun setSurfaceView(surfaceView: PlayerSurfaceView?) {
		exoPlayer.setVideoSurfaceView(surfaceView?.surface)
	}

	override fun setSubtitleView(surfaceView: PlayerSubtitleView?) {
		if (surfaceView != null) {
			if (subtitleView == null) {
				subtitleView = SubtitleView(surfaceView.context).apply {
					subtitleStyle?.let { style -> applySubtitleStyle(style) }
					if (exoPlayerOptions.enableLibass) {
						addView(AssSubtitleView(surfaceView.context, assHandler))
					}
				}
			}

			surfaceView.addView(subtitleView)
		} else {
			(subtitleView?.parent as? ViewGroup)?.removeView(subtitleView)
			subtitleView = null
		}
	}

	fun setSubtitleStyle(
		textColor: Int,
		backgroundColor: Int,
		strokeColor: Int,
		textWeight: Int,
		textSize: Float,
		bottomPaddingFraction: Float,
	) {
		val style = SubtitleStyle(
			textColor = textColor,
			backgroundColor = backgroundColor,
			strokeColor = strokeColor,
			textWeight = textWeight,
			textSize = textSize,
			bottomPaddingFraction = bottomPaddingFraction,
		)
		subtitleStyle = style
		subtitleView?.applySubtitleStyle(style)
	}

	private fun SubtitleView.applySubtitleStyle(style: SubtitleStyle) {
		setFixedTextSize(TypedValue.COMPLEX_UNIT_DIP, style.textSize)
		setBottomPaddingFraction(style.bottomPaddingFraction)
		setStyle(
			CaptionStyleCompat(
				style.textColor,
				style.backgroundColor,
				Color.TRANSPARENT,
				if (Color.alpha(style.strokeColor) == 0) CaptionStyleCompat.EDGE_TYPE_NONE else CaptionStyleCompat.EDGE_TYPE_OUTLINE,
				style.strokeColor,
				TypefaceCompat.create(context, Typeface.DEFAULT, style.textWeight, false),
			)
		)
	}

	private data class SubtitleStyle(
		val textColor: Int,
		val backgroundColor: Int,
		val strokeColor: Int,
		val textWeight: Int,
		val textSize: Float,
		val bottomPaddingFraction: Float,
	)

	override fun prepareItem(item: QueueEntry) {
		val stream = requireNotNull(item.mediaStream)
		val mediaItem = MediaItem.Builder().apply {
			setTag(item)
			setMediaId(stream.hashCode().toString())
			setUri(stream.url)
		}.build()

		// Remove any excessive items from the start
		while (exoPlayer.mediaItemCount > MEDIA_ITEM_COUNT_MAX - 1) exoPlayer.removeMediaItem(0)

		// Add new item to the end of the media item list
		exoPlayer.addMediaItem(mediaItem)

		// Instruct exoplayer to prepare
		exoPlayer.prepare()
	}

	override fun playItem(item: QueueEntry) {
		val stream = requireNotNull(item.mediaStream)
		if (currentStream == stream) return

		currentStream = stream

		var preparedItemIndex = (0 until exoPlayer.mediaItemCount).firstOrNull { index ->
			exoPlayer.getMediaItemAt(index).mediaId == stream.hashCode().toString()
		}

		// Prepare the item now if it doesn't exist yet
		if (preparedItemIndex == null) {
			prepareItem(item)
			preparedItemIndex = exoPlayer.mediaItemCount - 1
		}

		// Seek to prepared media item
		when (preparedItemIndex) {
			exoPlayer.currentMediaItemIndex - 1 -> exoPlayer.seekToPreviousMediaItem()
			exoPlayer.currentMediaItemIndex + 1 -> exoPlayer.seekToNextMediaItem()
			exoPlayer.currentMediaItemIndex -> Unit
			else -> exoPlayer.seekTo(preparedItemIndex, 0)
		}

		// Update audio attributes
		val contentType = when (item.mediaType) {
			MediaType.Video -> C.AUDIO_CONTENT_TYPE_MOVIE
			MediaType.Audio -> C.AUDIO_CONTENT_TYPE_MUSIC
			MediaType.Unknown -> C.AUDIO_CONTENT_TYPE_UNKNOWN
		}

		audioAttributeState.updateAudioAttributes(
			builder = {
				setContentType(contentType)
				setUsage(C.USAGE_MEDIA)
			},
			onChange = { audioAttributes ->
				exoPlayer.setAudioAttributes(audioAttributes, true)
			}
		)

		// Enjoy!
		Timber.i("Playing ${item.mediaStream?.url}")
		exoPlayer.play()
	}

	override fun play() {
		// If the item has ended, revert first so the item will start over again
		if (exoPlayer.playbackState == Player.STATE_ENDED) exoPlayer.seekTo(0)
		exoPlayer.play()
	}

	override fun pause() {
		exoPlayer.pause()
	}

	override fun stop() {
		exoPlayer.stop()
		currentStream = null
	}

	override fun seekTo(position: Duration) {
		if (!exoPlayer.isCommandAvailable(Player.COMMAND_SEEK_IN_CURRENT_MEDIA_ITEM) || !exoPlayer.isCurrentMediaItemSeekable) {
			Timber.w("Trying to seek but ExoPlayer doesn't support it for the current item")
		}

		exoPlayer.seekTo(position.inWholeMilliseconds)
	}

	override fun setScrubbing(scrubbing: Boolean) {
		exoPlayer.isScrubbingModeEnabled = scrubbing
	}

	override fun setSpeed(speed: Float) {
		if (!exoPlayer.isCommandAvailable(Player.COMMAND_SET_SPEED_AND_PITCH)) {
			Timber.w("Trying to change speed but ExoPlayer doesn't support it for the current item")
		}

		exoPlayer.setPlaybackSpeed(speed)
	}

	fun selectAudioTrack(groupIndex: Int, trackIndex: Int) {
		selectTrack(C.TRACK_TYPE_AUDIO, groupIndex, trackIndex)
	}

	fun selectSubtitleTrack(groupIndex: Int, trackIndex: Int) {
		selectTrack(C.TRACK_TYPE_TEXT, groupIndex, trackIndex)
	}

	fun disableSubtitles() {
		exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
			.buildUpon()
			.clearOverridesOfType(C.TRACK_TYPE_TEXT)
			.setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
			.build()
	}

	private fun selectTrack(type: Int, groupIndex: Int, trackIndex: Int) {
		val group = exoPlayer.currentTracks.groups.getOrNull(groupIndex) ?: return
		if (group.type != type || trackIndex !in 0 until group.length || !group.isTrackSupported(trackIndex)) return

		exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
			.buildUpon()
			.setTrackTypeDisabled(type, false)
			.setOverrideForType(TrackSelectionOverride(group.mediaTrackGroup, trackIndex))
			.build()
	}

	private fun Tracks.optionsForType(type: Int): List<PlayerTrackOption> = groups.flatMapIndexed { groupIndex, group ->
		if (group.type != type) return@flatMapIndexed emptyList()

		(0 until group.length).mapNotNull { trackIndex ->
			if (!group.isTrackSupported(trackIndex)) return@mapNotNull null
			val format = group.getTrackFormat(trackIndex)
			val language = format.language
				?.takeUnless { it == "und" }
				?.let { code -> Locale.forLanguageTag(code.replace('_', '-')).displayLanguage }
				?.replaceFirstChar { it.titlecase(Locale.getDefault()) }
			val rawCodec = format.codecs?.takeIf(String::isNotBlank)
				?: format.sampleMimeType?.substringAfter('/')
			val sourceLabel = format.label
				?.takeIf(String::isNotBlank)
				?.takeUnless { label ->
					label.equals(rawCodec, ignoreCase = true) ||
						label.startsWith("vnd.", ignoreCase = true) ||
						label.startsWith("audio/", ignoreCase = true)
				}
			val details = listOfNotNull(
				sourceLabel,
				language?.takeIf(String::isNotBlank),
				rawCodec?.friendlyCodecName(),
				format.channelCount.takeIf { type == C.TRACK_TYPE_AUDIO && it > 0 }?.friendlyChannelName(),
			).distinctBy { it.lowercase(Locale.getDefault()) }

			PlayerTrackOption(
				groupIndex = groupIndex,
				trackIndex = trackIndex,
				label = details.joinToString(" • ").ifBlank { "Track ${trackIndex + 1}" },
				selected = group.isTrackSelected(trackIndex),
			)
		}
	}

	private fun String.friendlyCodecName(): String {
		val codec = lowercase(Locale.ROOT)
		return when {
			codec.contains("dtsx") || codec.contains("dts-x") -> "DTS:X"
			codec.contains("dts.hd") || codec.contains("dts-hd") || codec.contains("dtshd") -> "DTS-HD"
			codec.contains("dts") -> "DTS"
			codec.contains("truehd") || codec.contains("mlp") -> "Dolby TrueHD"
			codec.contains("eac3") || codec.contains("e-ac-3") || codec.contains("ec-3") -> "Dolby Digital Plus"
			codec.contains("ac3") || codec.contains("ac-3") -> "Dolby Digital"
			codec.contains("ac4") || codec.contains("ac-4") -> "Dolby AC-4"
			codec.contains("mp4a") || codec.contains("aac") -> "AAC"
			codec.contains("opus") -> "Opus"
			codec.contains("vorbis") -> "Vorbis"
			codec.contains("flac") -> "FLAC"
			codec.contains("alac") -> "ALAC"
			codec.contains("mpeg") || codec.contains("mp3") -> "MP3"
			codec.contains("subrip") || codec.contains("srt") -> "SRT"
			codec.contains("ass") || codec.contains("ssa") -> "ASS"
			codec.contains("webvtt") || codec.contains("vtt") -> "WebVTT"
			codec.contains("pgs") -> "PGS"
			else -> this
		}
	}

	private fun Int.friendlyChannelName(): String = when (this) {
		1 -> "Mono"
		2 -> "Stereo"
		6 -> "5.1"
		8 -> "7.1"
		else -> "$this ch"
	}

	override fun getPositionInfo(): PositionInfo = PositionInfo(
		active = exoPlayer.currentPosition.milliseconds,
		buffer = exoPlayer.bufferedPosition.milliseconds,
		duration = lastKnownDuration ?: Duration.ZERO,
	)

	override fun setTimedEvents(timedEvents: List<TimedEvent>) {
		timedEventState.setTimedEvents(exoPlayer, timedEvents)
	}
}
