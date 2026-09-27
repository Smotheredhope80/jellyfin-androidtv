package org.jellyfin.androidtv.util

import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import java.util.UUID

/** Only TV children may use an untagged series-logo URL when inherited tags are absent. */
fun BaseItemDto.seriesLogoFallbackId(): UUID? = when (type) {
	BaseItemKind.EPISODE, BaseItemKind.SEASON -> seriesId
	else -> null
}
