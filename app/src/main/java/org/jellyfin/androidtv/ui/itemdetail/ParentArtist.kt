package org.jellyfin.androidtv.ui.itemdetail

import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import java.util.UUID

/** Prefer the album artist; track artists are a fallback when album metadata is incomplete. */
fun BaseItemDto.primaryArtistId(): UUID? {
	if (type != BaseItemKind.MUSIC_ALBUM && type != BaseItemKind.AUDIO) return null
	return (albumArtists.orEmpty() + artistItems.orEmpty())
		.mapNotNull { it.id }.firstOrNull { it != UUID(0, 0) }
}
