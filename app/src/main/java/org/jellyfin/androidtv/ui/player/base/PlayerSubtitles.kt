package org.jellyfin.androidtv.ui.player.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.playback.core.PlaybackManager
import org.jellyfin.playback.core.ui.PlayerSubtitleView
import org.jellyfin.playback.media3.exoplayer.ExoPlayerBackend
import org.koin.compose.koinInject

@Composable
fun PlayerSubtitles(
	modifier: Modifier = Modifier,
	playbackManager: PlaybackManager = koinInject(),
) {
	val userPreferences = koinInject<UserPreferences>()

	AndroidView(
		factory = { context -> PlayerSubtitleView(context) },
		modifier = modifier,
		update = { view ->
			view.playbackManager = playbackManager
			(playbackManager.backend as? ExoPlayerBackend)?.setSubtitleStyle(
				textColor = userPreferences[UserPreferences.subtitlesTextColor].toInt(),
				backgroundColor = userPreferences[UserPreferences.subtitlesBackgroundColor].toInt(),
				strokeColor = userPreferences[UserPreferences.subtitleTextStrokeColor].toInt(),
				textWeight = userPreferences[UserPreferences.subtitlesTextWeight],
				textSize = userPreferences[UserPreferences.subtitlesTextSize],
				bottomPaddingFraction = userPreferences[UserPreferences.subtitlesOffsetPosition],
			)
		}
	)
}
