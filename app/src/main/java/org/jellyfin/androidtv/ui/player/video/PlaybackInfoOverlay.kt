package org.jellyfin.androidtv.ui.player.video

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.composable.rememberQueueEntry
import org.jellyfin.playback.core.PlaybackManager
import org.jellyfin.playback.core.mediastream.MediaConversionMethod
import org.jellyfin.playback.core.mediastream.MediaStreamAudioTrack
import org.jellyfin.playback.core.mediastream.MediaStreamVideoTrack
import org.jellyfin.playback.core.mediastream.mediaStreamFlow
import org.jellyfin.playback.jellyfin.queue.baseItem
import org.jellyfin.playback.jellyfin.queue.baseItemFlow
import org.jellyfin.sdk.model.api.MediaStreamType
import org.jellyfin.sdk.model.api.VideoRangeType

@Composable
fun PlaybackInfoOverlay(
	playbackManager: PlaybackManager,
	modifier: Modifier = Modifier,
	onDismiss: () -> Unit = {},
) {
	val entry by rememberQueueEntry(playbackManager)
	val mediaStream by entry?.mediaStreamFlow?.collectAsState(null) ?: return
	val stream = mediaStream ?: return
	val item = entry?.run { baseItemFlow.collectAsState(baseItem) }?.value

	val videoTrack = stream.tracks.filterIsInstance<MediaStreamVideoTrack>().firstOrNull()
	val audioTrack = stream.tracks.filterIsInstance<MediaStreamAudioTrack>().firstOrNull()

	val serverVideo = item?.mediaStreams?.firstOrNull { it.type == MediaStreamType.VIDEO }
	val serverAudio = item?.mediaStreams?.firstOrNull { it.type == MediaStreamType.AUDIO }

	val cardShape = RoundedCornerShape(14.dp)

	Column(
		verticalArrangement = Arrangement.spacedBy(10.dp),
		modifier = modifier
			.width(540.dp)
			.background(Color(0xEE070A12), cardShape)
			.border(1.5.dp, Color(0x99775BF4), cardShape)
			.padding(horizontal = 20.dp, vertical = 16.dp)
	) {
		// Top Header Row
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = "SHIELD PLAYBACK HUD",
				style = TextStyle(
					color = Color(0xFFC084FC),
					fontSize = 12.sp,
					fontWeight = FontWeight.Bold,
					letterSpacing = 1.sp,
				)
			)

			// Play method badge
			when (stream.conversionMethod) {
				MediaConversionMethod.None -> {
					HudBadge(
						text = stringResource(R.string.playback_info_direct_play).uppercase(),
						textColor = Color(0xFF34D399),
						backgroundColor = Color(0x3310B981),
						borderColor = Color(0x8010B981)
					)
				}
				MediaConversionMethod.Remux -> {
					HudBadge(
						text = stringResource(R.string.playback_info_direct_stream).uppercase(),
						textColor = Color(0xFF38BDF8),
						backgroundColor = Color(0x3306B6D4),
						borderColor = Color(0x8006B6D4)
					)
				}
				MediaConversionMethod.Transcode -> {
					HudBadge(
						text = stringResource(R.string.playback_info_transcoding).uppercase(),
						textColor = Color(0xFFFBBF24),
						backgroundColor = Color(0x33F59E0B),
						borderColor = Color(0x80F59E0B)
					)
				}
			}
		}

		// Subtle Divider
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(1.dp)
				.background(Color(0x2E775BF4))
		)

		// 2-Column Content
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(20.dp)
		) {
			// Left Column: Video Stream
			Column(
				modifier = Modifier.weight(1f),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Text(
					text = stringResource(R.string.playback_info_video_title).uppercase(),
					style = TextStyle(
						color = Color(0xFF94A3B8),
						fontSize = 11.sp,
						fontWeight = FontWeight.Bold,
						letterSpacing = 1.sp,
					)
				)

				// Video Codec & Resolution Badges
				Row(
					horizontalArrangement = Arrangement.spacedBy(6.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					videoTrack?.codec?.let { codec ->
						Text(
							text = codec.uppercase(),
							style = TextStyle(
								color = Color.White,
								fontSize = 14.sp,
								fontWeight = FontWeight.Bold,
							)
						)
					}

					val width = videoTrack?.width ?: serverVideo?.width ?: 0
					val height = videoTrack?.height ?: serverVideo?.height ?: 0
					if (width >= 3840 || height >= 2160) {
						HudBadge(
							text = "4K UHD",
							textColor = Color(0xFF34D399),
							backgroundColor = Color(0x33059669),
							borderColor = Color(0x8010B981)
						)
					} else if (width >= 1920 || height >= 1080) {
						HudBadge(
							text = "1080p",
							textColor = Color(0xFF93C5FD),
							backgroundColor = Color(0x2E2563EB),
							borderColor = Color(0x803B82F6)
						)
					} else if (height > 0) {
						HudBadge(
							text = "${height}p",
							textColor = Color(0xFFCBD5E1),
							backgroundColor = Color(0x2E475569),
							borderColor = Color(0x8064748B)
						)
					}
				}

				// Resolution details
				val resWidth = videoTrack?.width ?: serverVideo?.width ?: 0
				val resHeight = videoTrack?.height ?: serverVideo?.height ?: 0
				if (resWidth > 0 && resHeight > 0) {
					StatItem(
						label = "Resolution",
						value = "$resWidth × $resHeight"
					)
				}

				// Video Range / Dynamic Range Badge
				val isDovi = serverVideo?.videoRangeType in listOf(
					VideoRangeType.DOVI, VideoRangeType.DOVI_WITH_EL, VideoRangeType.DOVI_WITH_HDR10,
					VideoRangeType.DOVI_WITH_HLG, VideoRangeType.DOVI_WITH_SDR, VideoRangeType.DOVI_WITH_HDR10_PLUS,
					VideoRangeType.DOVI_WITH_ELHDR10_PLUS
				) || videoTrack?.videoRange?.contains("DOVI", ignoreCase = true) == true

				val isHdr10Plus = serverVideo?.videoRangeType == VideoRangeType.HDR10_PLUS ||
					videoTrack?.videoRange?.contains("HDR10+", ignoreCase = true) == true
				val isHdr = serverVideo?.videoRangeType == VideoRangeType.HDR10 ||
					serverVideo?.videoRangeType == VideoRangeType.HLG ||
					videoTrack?.videoRange?.contains("HDR", ignoreCase = true) == true

				when {
					isDovi -> {
						Row(
							horizontalArrangement = Arrangement.spacedBy(6.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							HudBadge(
								text = "DOLBY VISION",
								textColor = Color(0xFFD8B4FE),
								backgroundColor = Color(0x38A855F7),
								borderColor = Color(0x80A855F7)
							)
							serverVideo?.videoDoViTitle?.takeIf { it.isNotBlank() }?.let { doviTitle ->
								Text(
									text = doviTitle,
									style = TextStyle(color = Color(0xFFCBD5E1), fontSize = 11.sp)
								)
							}
						}
					}
					isHdr10Plus -> {
						HudBadge(
							text = "HDR10+",
							textColor = Color(0xFFFDE047),
							backgroundColor = Color(0x38EAB308),
							borderColor = Color(0x80EAB308)
						)
					}
					isHdr -> {
						HudBadge(
							text = "HDR10",
							textColor = Color(0xFFFDE047),
							backgroundColor = Color(0x38EAB308),
							borderColor = Color(0x80EAB308)
						)
					}
					else -> {
						StatItem(
							label = "Color Range",
							value = videoTrack?.videoRange ?: "SDR"
						)
					}
				}

				// Video Bitrate
				val videoBitrate = videoTrack?.bitrate?.takeIf { it > 0 } ?: serverVideo?.bitRate?.toInt() ?: 0
				if (videoBitrate > 0) {
					StatItem(
						label = "Video Bitrate",
						value = formatBitrate(videoBitrate),
						valueColor = Color(0xFF34D399)
					)
				}

				// Frame Rate & Bit Depth
				val frameRate = serverVideo?.averageFrameRate ?: serverVideo?.realFrameRate
				val bitDepth = serverVideo?.bitDepth
				if (frameRate != null || bitDepth != null) {
					val parts = buildList {
						frameRate?.let { add("%.3f fps".format(it)) }
						bitDepth?.let { add("$it-bit") }
					}
					StatItem(
						label = "Framerate & Depth",
						value = parts.joinToString(" • ")
					)
				}
			}

			// Right Column: Audio & Container
			Column(
				modifier = Modifier.weight(1f),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Text(
					text = stringResource(R.string.playback_info_audio_title).uppercase(),
					style = TextStyle(
						color = Color(0xFF94A3B8),
						fontSize = 11.sp,
						fontWeight = FontWeight.Bold,
						letterSpacing = 1.sp,
					)
				)

				// Audio Codec & Atmos Badge
				val isAtmos = serverAudio?.profile?.contains("Dolby Atmos", ignoreCase = true) == true ||
					serverAudio?.title?.contains("atmos", ignoreCase = true) == true

				Row(
					horizontalArrangement = Arrangement.spacedBy(6.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					val audioCodec = audioTrack?.codec ?: serverAudio?.codec
					audioCodec?.let { codec ->
						Text(
							text = codec.uppercase(),
							style = TextStyle(
								color = Color.White,
								fontSize = 14.sp,
								fontWeight = FontWeight.Bold,
							)
						)
					}

					if (isAtmos) {
						HudBadge(
							text = "DOLBY ATMOS",
							textColor = Color(0xFF7DD3FC),
							backgroundColor = Color(0x380EA5E9),
							borderColor = Color(0x800EA5E9)
						)
					}
				}

				// Audio Channels
				val channels = audioTrack?.channels ?: serverAudio?.channels ?: 0
				val channelLayout = serverAudio?.channelLayout
				if (channels > 0 || !channelLayout.isNullOrBlank()) {
					StatItem(
						label = "Channels",
						value = formatChannels(channels, channelLayout)
					)
				}

				// Audio Bitrate
				val audioBitrate = audioTrack?.bitrate?.takeIf { it > 0 } ?: serverAudio?.bitRate?.toInt() ?: 0
				if (audioBitrate > 0) {
					StatItem(
						label = "Audio Bitrate",
						value = formatBitrate(audioBitrate),
						valueColor = Color(0xFF38BDF8)
					)
				}

				// Sample Rate
				val sampleRate = audioTrack?.sampleRate?.takeIf { it > 0 } ?: serverAudio?.sampleRate ?: 0
				if (sampleRate > 0) {
					StatItem(
						label = "Sample Rate",
						value = "${sampleRate / 1000} kHz"
					)
				}

				// Container Format
				StatItem(
					label = "Container",
					value = stream.container.format.uppercase()
				)
			}
		}

		// Transcoding Note (if active)
		if (stream.conversionMethod == MediaConversionMethod.Transcode) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.background(Color(0x1AF59E0B), RoundedCornerShape(8.dp))
					.border(1.dp, Color(0x66F59E0B), RoundedCornerShape(8.dp))
					.padding(10.dp)
			) {
				Text(
					text = stringResource(R.string.playback_info_transcoding_note),
					style = TextStyle(
						color = Color(0xFFFDE68A),
						fontSize = 12.sp,
						fontWeight = FontWeight.Medium,
					)
				)
			}
		}
	}
}

@Composable
private fun StatItem(
	label: String,
	value: String,
	modifier: Modifier = Modifier,
	valueColor: Color = Color.White,
) {
	Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
		Text(
			text = label.uppercase(),
			style = TextStyle(
				color = Color(0xFF94A3B8),
				fontSize = 11.sp,
				fontWeight = FontWeight.Medium,
				letterSpacing = 0.5.sp,
			)
		)
		Text(
			text = value,
			style = TextStyle(
				color = valueColor,
				fontSize = 13.sp,
				fontWeight = FontWeight.SemiBold,
			)
		)
	}
}

@Composable
private fun HudBadge(
	text: String,
	textColor: Color,
	backgroundColor: Color,
	borderColor: Color,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.background(backgroundColor, RoundedCornerShape(6.dp))
			.border(1.dp, borderColor, RoundedCornerShape(6.dp))
			.padding(horizontal = 8.dp, vertical = 3.dp)
	) {
		Text(
			text = text,
			style = TextStyle(
				color = textColor,
				fontSize = 11.sp,
				fontWeight = FontWeight.Bold,
				letterSpacing = 0.5.sp,
			)
		)
	}
}

private fun formatBitrate(bitrate: Int): String {
	return when {
		bitrate >= 1_000_000 -> "%.1f Mbps".format(bitrate / 1_000_000f)
		bitrate > 0 -> "%.0f kbps".format(bitrate / 1_000f)
		else -> "—"
	}
}

private fun formatChannels(channels: Int, layout: String? = null): String {
	return when (channels) {
		8 -> "7.1 Surround (8 ch)"
		6 -> "5.1 Surround (6 ch)"
		2 -> "Stereo (2 ch)"
		1 -> "Mono (1 ch)"
		else -> if (channels > 0) "$channels channels" else layout ?: "—"
	}
}
