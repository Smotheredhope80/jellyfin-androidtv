package org.jellyfin.androidtv.ui.player.video

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.ui.base.Icon
import org.jellyfin.androidtv.ui.base.JellyfinTheme
import org.jellyfin.androidtv.ui.base.LocalTextStyle
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.base.button.Button
import org.jellyfin.androidtv.ui.base.button.ButtonDefaults
import org.jellyfin.androidtv.ui.base.button.IconButton
import org.jellyfin.androidtv.ui.base.popover.Popover
import org.jellyfin.androidtv.ui.composable.rememberPlayerPositionInfo
import org.jellyfin.androidtv.ui.player.base.PlayerSeekbar
import org.jellyfin.androidtv.util.getTimeFormatter
import org.jellyfin.androidtv.util.apiclient.chapterImages
import org.jellyfin.androidtv.util.apiclient.getUrl
import org.jellyfin.playback.core.PlaybackManager
import org.jellyfin.playback.core.model.PlayState
import org.jellyfin.playback.core.queue.queue
import org.jellyfin.playback.media3.exoplayer.ExoPlayerBackend
import org.jellyfin.playback.media3.exoplayer.PlayerTrackOption
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.ChapterInfo
import org.jellyfin.sdk.model.extensions.ticks
import org.jellyfin.sdk.api.client.ApiClient
import org.koin.compose.koinInject
import java.time.LocalDateTime
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@Composable
fun VideoPlayerControls(
	playbackManager: PlaybackManager = koinInject(),
	item: BaseItemDto? = null,
	chapterBrowserVisible: Boolean = false,
	onChapterBrowserVisibleChange: (Boolean) -> Unit = {},
	onPlaybackInfoClick: () -> Unit = {},
) {
	val playState by playbackManager.state.playState.collectAsState()
	val exoPlayerBackend = playbackManager.backend as? ExoPlayerBackend

	androidx.compose.runtime.LaunchedEffect(item?.id) {
		onChapterBrowserVisibleChange(false)
	}

	Column(
		verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom),
	) {
		if (chapterBrowserVisible && item != null) {
			ChapterStrip(
				playbackManager = playbackManager,
				item = item,
				onDismiss = { onChapterBrowserVisibleChange(false) },
			)
		} else {
			Row(
				horizontalArrangement = Arrangement.spacedBy(12.dp),
				modifier = Modifier
					.focusRestorer()
					.focusGroup()
			) {
				PlayPauseButton(playbackManager, playState)
				RewindButton(playbackManager)
				FastForwardButton(playbackManager)
				ChapterSelectButton(
					chapters = item?.chapters.orEmpty(),
					expanded = false,
					onToggle = { onChapterBrowserVisibleChange(true) },
				)
				PlaybackSpeedButton(playbackManager)
				exoPlayerBackend?.let {
					AudioTrackButton(it)
					SubtitleTrackButton(it)
				}

				Spacer(Modifier.weight(1f))

				PlaybackInfoButton(onClick = onPlaybackInfoClick)

				MoreOptionsButton {
					PreviousEntryButton(playbackManager)
					NextEntryButton(playbackManager)
				}
			}

			PlayerSeekbar(
				playbackManager = playbackManager,
				modifier = Modifier
					.fillMaxWidth()
					.height(4.dp)
			)

			PlaybackTimingRow(playbackManager)
		}
	}
}

@Composable
private fun AudioTrackButton(
	backend: ExoPlayerBackend,
) = Box {
	val tracks by backend.audioTracks.collectAsState()
	var expanded by remember { mutableStateOf(false) }

	IconButton(
		enabled = tracks.isNotEmpty(),
		onClick = { expanded = true },
	) {
		Icon(
			imageVector = ImageVector.vectorResource(R.drawable.ic_select_audio),
			contentDescription = stringResource(R.string.lbl_audio_track),
		)
	}

	TrackSelectionPopover(
		expanded = expanded,
		onDismissRequest = { expanded = false },
		tracks = tracks,
		onTrackSelected = { track ->
			backend.selectAudioTrack(track.groupIndex, track.trackIndex)
			expanded = false
		},
	)
}

@Composable
private fun SubtitleTrackButton(
	backend: ExoPlayerBackend,
) = Box {
	val tracks by backend.subtitleTracks.collectAsState()
	var expanded by remember { mutableStateOf(false) }

	IconButton(
		enabled = tracks.isNotEmpty(),
		onClick = { expanded = true },
	) {
		Icon(
			imageVector = ImageVector.vectorResource(R.drawable.ic_select_subtitle),
			contentDescription = stringResource(R.string.lbl_subtitle_track),
		)
	}

	TrackSelectionPopover(
		expanded = expanded,
		onDismissRequest = { expanded = false },
		tracks = tracks,
		includeNone = true,
		onNoneSelected = {
			backend.disableSubtitles()
			expanded = false
		},
		onTrackSelected = { track ->
			backend.selectSubtitleTrack(track.groupIndex, track.trackIndex)
			expanded = false
		},
	)
}

@Composable
private fun TrackSelectionPopover(
	expanded: Boolean,
	onDismissRequest: () -> Unit,
	tracks: List<PlayerTrackOption>,
	includeNone: Boolean = false,
	onNoneSelected: () -> Unit = {},
	onTrackSelected: (PlayerTrackOption) -> Unit,
) {
	Popover(
		expanded = expanded,
		onDismissRequest = onDismissRequest,
		alignment = Alignment.TopCenter,
		offset = DpOffset(0.dp, (-5).dp),
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(6.dp),
			modifier = Modifier
				.width(360.dp)
				.heightIn(max = 420.dp)
				.verticalScroll(rememberScrollState())
				.padding(8.dp),
		) {
			if (includeNone) {
				Button(
					onClick = onNoneSelected,
					modifier = Modifier.fillMaxWidth(),
				) {
					val subtitlesEnabled = tracks.any(PlayerTrackOption::selected)
					Text(if (subtitlesEnabled) stringResource(R.string.lbl_none) else "✓  ${stringResource(R.string.lbl_none)}")
				}
			}

			tracks.forEach { track ->
				Button(
					onClick = { onTrackSelected(track) },
					modifier = Modifier.fillMaxWidth(),
				) {
					Text(if (track.selected) "✓  ${track.label}" else track.label)
				}
			}
		}
	}
}

@Composable
private fun ChapterSelectButton(
	chapters: List<ChapterInfo>,
	expanded: Boolean,
	onToggle: () -> Unit,
) {
	IconButton(
		enabled = chapters.isNotEmpty(),
		onClick = onToggle,
	) {
		Icon(
			imageVector = ImageVector.vectorResource(R.drawable.ic_select_chapter),
			contentDescription = stringResource(R.string.lbl_chapters) + if (expanded) " ✓" else "",
		)
	}
}

@Composable
private fun ChapterStrip(
	playbackManager: PlaybackManager,
	item: BaseItemDto,
	onDismiss: () -> Unit,
) {
	val api = koinInject<ApiClient>()
	val chapters = item.chapters.orEmpty()
	val images = item.chapterImages
	val firstChapterFocusRequester = remember { FocusRequester() }

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(194.dp),
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(194.dp)
				.background(Color.Black.copy(alpha = 0.45f))
		)

		Row(
			horizontalArrangement = Arrangement.spacedBy(0.dp),
			modifier = Modifier
				.fillMaxWidth()
				.horizontalScroll(rememberScrollState())
				.padding(top = 24.dp)
				.onKeyEvent { event ->
					if (event.key == Key.Back) {
						onDismiss()
						true
					} else false
				},
		) {
		chapters.forEachIndexed { index, chapter ->
			var focused by remember(item.id, index) { mutableStateOf(false) }
			val image = images.getOrNull(index)?.takeIf { it.tag.isNotEmpty() }
			val chapterName = chapter.name ?: stringResource(R.string.lbl_chapter_number, index + 1)
			val chapterPosition = chapter.startPositionTicks.ticks
			val chapterTime = chapterPosition.formatted(includeHours = chapterPosition.inWholeHours > 0)
			Button(
				onClick = {
					playbackManager.state.seek(chapterPosition)
					onDismiss()
				},
				modifier = Modifier
					.width(196.dp)
					.height(170.dp)
					.onFocusChanged { focused = it.isFocused }
					.then(if (index == 0) Modifier.focusRequester(firstChapterFocusRequester) else Modifier),
				shape = JellyfinTheme.shapes.small,
				colors = ButtonDefaults.colors(
					containerColor = Color.Transparent,
					contentColor = Color.White,
					focusedContainerColor = Color.Transparent,
					focusedContentColor = Color.White,
				),
				contentPadding = PaddingValues(0.dp),
			) {
				Column(
					verticalArrangement = Arrangement.spacedBy(4.dp),
					modifier = Modifier.fillMaxWidth(),
				) {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.height(110.dp)
							.background(Color.DarkGray)
							.border(
								width = if (focused) 6.dp else 0.dp,
								color = if (focused) Color.White else Color.Transparent,
								shape = JellyfinTheme.shapes.small,
							)
							.padding(if (focused) 6.dp else 0.dp)
							.clipToBounds(),
					) {
						Image(
							painter = rememberAsyncImagePainter(image?.getUrl(api, maxWidth = 392, maxHeight = 220)),
							contentDescription = null,
							contentScale = ContentScale.Crop,
							modifier = Modifier.fillMaxSize(),
						)
					}
					Text(
						text = "$chapterName\n$chapterTime",
						modifier = Modifier
							.fillMaxWidth()
							.height(48.dp)
							.padding(start = 4.dp, top = 2.dp),
						fontSize = 16.sp,
						lineHeight = 20.sp,
						fontWeight = FontWeight.Medium,
						maxLines = 2,
					)
				}
			}
		}
	}
	}

	androidx.compose.runtime.LaunchedEffect(item.id) {
		firstChapterFocusRequester.requestFocus()
	}
}

@Composable
private fun PlaybackSpeedButton(
	playbackManager: PlaybackManager,
) = Box {
	val currentSpeed by playbackManager.state.speed.collectAsState()
	var expanded by remember { mutableStateOf(false) }
	val speeds = remember { listOf(0.25f, 0.5f, 0.75f, 1f, 1.25f, 1.5f, 1.75f, 2f) }

	IconButton(onClick = { expanded = true }) {
		Icon(
			imageVector = ImageVector.vectorResource(R.drawable.ic_playback_speed),
			contentDescription = stringResource(R.string.lbl_playback_speed),
		)
	}

	Popover(
		expanded = expanded,
		onDismissRequest = { expanded = false },
		alignment = Alignment.TopCenter,
		offset = DpOffset(0.dp, (-5).dp),
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(6.dp),
			modifier = Modifier
				.width(180.dp)
				.heightIn(max = 420.dp)
				.verticalScroll(rememberScrollState())
				.padding(8.dp),
		) {
			speeds.forEach { speed ->
				val label = String.format(Locale.US, "%.2fx", speed)
				Button(
					onClick = {
						playbackManager.state.setSpeed(speed)
						expanded = false
					},
					modifier = Modifier.fillMaxWidth(),
				) {
					Text(if (currentSpeed == speed) "✓  $label" else label)
				}
			}
		}
	}
}

@Composable
private fun PlayPauseButton(
	playbackManager: PlaybackManager,
	playState: PlayState,
) {
	val focusRequester = remember { FocusRequester() }
	IconButton(
		onClick = {
			when (playState) {
				PlayState.STOPPED,
				PlayState.ERROR -> playbackManager.state.play()

				PlayState.PLAYING -> playbackManager.state.pause()
				PlayState.PAUSED -> playbackManager.state.unpause()
			}
		},
		modifier = Modifier
			.focusRequester(focusRequester)
			.onVisibilityChanged {
				focusRequester.requestFocus()
			}
	) {
		AnimatedContent(playState) { playState ->
			when (playState) {
				PlayState.PLAYING -> {
					Icon(
						imageVector = ImageVector.vectorResource(R.drawable.ic_pause),
						contentDescription = stringResource(R.string.lbl_pause),
					)
				}

				PlayState.STOPPED,
				PlayState.PAUSED,
				PlayState.ERROR -> {
					Icon(
						imageVector = ImageVector.vectorResource(R.drawable.ic_play),
						contentDescription = stringResource(R.string.lbl_play),
					)
				}
			}
		}
	}
}

@Composable
private fun RewindButton(
	playbackManager: PlaybackManager,
) = IconButton(
	onClick = { playbackManager.state.rewind() },
) {
	Icon(
		imageVector = ImageVector.vectorResource(R.drawable.ic_rewind),
		contentDescription = stringResource(R.string.rewind),
	)
}

@Composable
private fun FastForwardButton(
	playbackManager: PlaybackManager,
) = IconButton(
	onClick = { playbackManager.state.fastForward() },
) {
	Icon(
		imageVector = ImageVector.vectorResource(R.drawable.ic_fast_forward),
		contentDescription = stringResource(R.string.fast_forward),
	)
}

@Composable
private fun PreviousEntryButton(
	playbackManager: PlaybackManager,
) {
	val entryIndex by playbackManager.queue.entryIndex.collectAsState()
	val coroutineScope = rememberCoroutineScope()

	IconButton(
		enabled = entryIndex > 0,
		onClick = {
			coroutineScope.launch {
				playbackManager.queue.previous()
			}
		},
	) {
		Icon(
			imageVector = ImageVector.vectorResource(R.drawable.ic_previous),
			contentDescription = stringResource(R.string.lbl_prev_item),
		)
	}
}

@Composable
private fun NextEntryButton(
	playbackManager: PlaybackManager,
) {
	val entryIndex by playbackManager.queue.entryIndex.collectAsState()
	val coroutineScope = rememberCoroutineScope()

	IconButton(
		enabled = entryIndex < playbackManager.queue.estimatedSize - 1,
		onClick = {
			coroutineScope.launch {
				playbackManager.queue.next()
			}
		},
	) {
		Icon(
			imageVector = ImageVector.vectorResource(R.drawable.ic_next),
			contentDescription = stringResource(R.string.lbl_next_item),
		)
	}
}

private fun Duration.formatted(includeHours: Boolean): String {
	val totalSeconds = toInt(DurationUnit.SECONDS)
	val hours = totalSeconds / 3600
	val minutes = (totalSeconds % 3600) / 60
	val seconds = totalSeconds % 60

	return if (includeHours) "%02d:%02d:%02d".format(hours, minutes, seconds)
	else "%02d:%02d".format(minutes, seconds)
}

@Composable
private fun PlaybackTimingRow(
	playbackManager: PlaybackManager,
) {
	val context = LocalContext.current
	val positionInfo by rememberPlayerPositionInfo(playbackManager, precision = 1.seconds)
	val speed by playbackManager.state.speed.collectAsState()
	var now by remember { mutableStateOf(LocalDateTime.now()) }

	// Keep the wall clock and calculated finish time current even while paused.
	androidx.compose.runtime.LaunchedEffect(Unit) {
		while (true) {
			now = LocalDateTime.now()
			delay(1.seconds)
		}
	}

	if (positionInfo.duration == Duration.ZERO) return

	val positionText by remember {
		derivedStateOf {
			val includeHours = positionInfo.duration.inWholeMinutes >= 60
			val activeFormatted = positionInfo.active.formatted(includeHours)
			val durationFormatted = positionInfo.duration.formatted(includeHours)

			"$activeFormatted / $durationFormatted"
		}
	}
	val realTimeRemaining = ((positionInfo.duration - positionInfo.active).coerceAtLeast(Duration.ZERO) /
		speed.coerceAtLeast(0.1f).toDouble())
	val endTime = now.plusNanos(realTimeRemaining.inWholeNanoseconds)
	val timeFormatter = remember(context) { context.getTimeFormatter() }
	val currentTimeText = timeFormatter.format(now)
	val remainingText = stringResource(R.string.lbl_playback_control_remaining, realTimeRemaining.formatted(includeHours = true))
	val endsText = stringResource(R.string.lbl_playback_control_ends, timeFormatter.format(endTime))

	Row(
		horizontalArrangement = Arrangement.spacedBy(16.dp),
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.fillMaxWidth(),
	) {
		Text(
			text = "$currentTimeText  •  $remainingText  •  $endsText",
			style = LocalTextStyle.current.copy(color = Color.White)
		)
		Spacer(Modifier.weight(1f))
		Text(
			text = positionText,
			style = LocalTextStyle.current.copy(color = Color.White)
		)
	}
}

@Composable
private fun MoreOptionsButton(
	content: @Composable () -> Unit,
) = Box {
	var expanded by remember { mutableStateOf(false) }
	IconButton(
		onClick = { expanded = true },
	) {
		Icon(
			imageVector = ImageVector.vectorResource(R.drawable.ic_more),
			contentDescription = stringResource(R.string.lbl_other_options),
		)
	}

	Popover(
		expanded = expanded,
		onDismissRequest = { expanded = false },
		alignment = Alignment.TopCenter,
		offset = DpOffset(0.dp, (-5).dp)
	) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(12.dp),
			modifier = Modifier
				.padding(4.dp)
		) {
			content()
		}
	}
}

@Composable
fun PlaybackInfoButton(
	onClick: () -> Unit,
) = IconButton(
	onClick = onClick,
) {
	Icon(
		imageVector = ImageVector.vectorResource(R.drawable.ic_info),
		contentDescription = stringResource(R.string.playback_info),
	)
}
