package org.jellyfin.androidtv.ui.home

import org.jellyfin.sdk.model.api.BaseItemDto

/** A display-only projection. Never writes or supplies the player's resume position. */
internal data class HomePlaybackProgress(val positionMillis: Long, val remainingMillis: Long, val fraction: Float)

internal fun homePlaybackProgress(positionTicks: Long?, runtimeTicks: Long?): HomePlaybackProgress? {
	val runtime = runtimeTicks?.takeIf { it > 0 } ?: return null
	val position = positionTicks?.takeIf { it > 0 && it < runtime } ?: return null
	return HomePlaybackProgress(position / 10_000, (runtime - position) / 10_000, (position.toDouble() / runtime).toFloat())
}

internal fun BaseItemDto.homePlaybackProgress() = homePlaybackProgress(userData?.playbackPositionTicks, runTimeTicks)
