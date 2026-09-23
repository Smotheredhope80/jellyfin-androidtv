package org.jellyfin.playback.media3.exoplayer

import androidx.media3.common.Player
import kotlin.time.Duration

internal fun selectPreparedMediaItem(player: Player, index: Int, initialPosition: Duration?) {
	if (initialPosition != null) {
		player.seekTo(index, initialPosition.inWholeMilliseconds)
	} else when (index) {
		player.currentMediaItemIndex - 1 -> player.seekToPreviousMediaItem()
		player.currentMediaItemIndex + 1 -> player.seekToNextMediaItem()
		player.currentMediaItemIndex -> Unit
		else -> player.seekTo(index, 0)
	}
}
