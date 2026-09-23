package org.jellyfin.androidtv.ui.itemdetail

import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind

fun BaseItemDto.supportsThemeMusic() = type == BaseItemKind.SERIES || type == BaseItemKind.SEASON

/** Invalidates both network and prepared-player callbacks when navigation stops a theme. */
class ThemeMusicGate {
	private var generation = 0L
	private var active = false
	fun begin(): Long { active = true; return ++generation }
	fun stop() { active = false; generation++ }
	fun accepts(ticket: Long) = active && ticket == generation
}
