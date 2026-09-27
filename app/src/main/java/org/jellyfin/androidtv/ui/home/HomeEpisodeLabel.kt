package org.jellyfin.androidtv.ui.home

import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind

/** Episode identity remains visible even when the card also has resume progress. */
fun BaseItemDto.homeEpisodeLabel(): String? {
	if (type != BaseItemKind.EPISODE) return null
	val number = listOfNotNull(parentIndexNumber?.let { "S$it" }, indexNumber?.let { "E$it" })
		.joinToString(":").takeIf(String::isNotBlank)
	return listOfNotNull(number, name?.takeIf(String::isNotBlank)).joinToString(" - ").takeIf(String::isNotBlank)
}
