package org.jellyfin.playback.media3.exoplayer

/** A track exposed by the active Media3 player for in-session selection. */
data class PlayerTrackOption(
	val groupIndex: Int,
	val trackIndex: Int,
	val label: String,
	val selected: Boolean,
)
