package org.jellyfin.androidtv.ui.home

import androidx.annotation.DrawableRes
import org.jellyfin.androidtv.R
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.CollectionType
import java.util.Locale

/** Local presentation only: never substitute artwork on individual media items. */
@DrawableRes
fun BaseItemDto.libraryArtwork(): Int? {
	if (type != BaseItemKind.USER_VIEW && type != BaseItemKind.COLLECTION_FOLDER) return null
	val normalizedName = name.orEmpty().lowercase(Locale.ROOT)
		.replace(Regex("[\\s\\p{Pd}_]+"), " ").trim()
	return when (normalizedName) {
		"music staging" -> R.drawable.library_music_staging
		"music videos", "musicvideos" -> R.drawable.library_music_videos
		"movies" -> R.drawable.library_movies
		"tv shows", "tvshows" -> R.drawable.library_tv_shows
		"music" -> R.drawable.library_music
		"collections" -> R.drawable.library_collections
		"playlists" -> R.drawable.library_playlists
		else -> when (collectionType) {
			CollectionType.MOVIES -> R.drawable.library_movies
			CollectionType.TVSHOWS -> R.drawable.library_tv_shows
			CollectionType.MUSIC -> R.drawable.library_music
			CollectionType.MUSICVIDEOS -> R.drawable.library_music_videos
			CollectionType.BOXSETS -> R.drawable.library_collections
			CollectionType.PLAYLISTS -> R.drawable.library_playlists
			else -> null
		}
	}
}

data class ElegantFinLibraryCover(
	@DrawableRes val drawableRes: Int,
	val overlayColorHex: Long,
)

/** Local presentation only: never substitute artwork on individual media items. */
fun BaseItemDto.elegantFinMediaCover(): ElegantFinLibraryCover? {
	if (type != BaseItemKind.USER_VIEW && type != BaseItemKind.COLLECTION_FOLDER) return null
	val normalizedName = name.orEmpty().lowercase(Locale.ROOT)
		.replace(Regex("[\\s\\p{Pd}_]+"), " ").trim()
	return when (normalizedName) {
		"movies" -> ElegantFinLibraryCover(R.drawable.ef_cover_movies, 0xFFC16768)
		"tv shows", "tvshows" -> ElegantFinLibraryCover(R.drawable.ef_cover_tvshows, 0xFF8C952B)
		"music staging" -> ElegantFinLibraryCover(R.drawable.ef_cover_folders, 0xFFAD3C71)
		"music videos", "musicvideos" -> ElegantFinLibraryCover(R.drawable.ef_cover_homevideos, 0xFF275AB9)
		"music" -> ElegantFinLibraryCover(R.drawable.ef_cover_music, 0xFF0B5D48)
		"collections" -> ElegantFinLibraryCover(R.drawable.ef_cover_boxsets, 0xFFDBB435)
		"playlists" -> ElegantFinLibraryCover(R.drawable.ef_cover_playlists, 0xFF763DD8)
		"live tv", "livetv" -> ElegantFinLibraryCover(R.drawable.ef_cover_livetv, 0xFF11629F)
		else -> when (collectionType) {
			CollectionType.MOVIES -> ElegantFinLibraryCover(R.drawable.ef_cover_movies, 0xFFC16768)
			CollectionType.TVSHOWS -> ElegantFinLibraryCover(R.drawable.ef_cover_tvshows, 0xFF8C952B)
			CollectionType.MUSIC -> ElegantFinLibraryCover(R.drawable.ef_cover_music, 0xFF0B5D48)
			CollectionType.MUSICVIDEOS -> ElegantFinLibraryCover(R.drawable.ef_cover_homevideos, 0xFF275AB9)
			CollectionType.BOXSETS -> ElegantFinLibraryCover(R.drawable.ef_cover_boxsets, 0xFFDBB435)
			CollectionType.PLAYLISTS -> ElegantFinLibraryCover(R.drawable.ef_cover_playlists, 0xFF763DD8)
			CollectionType.LIVETV -> ElegantFinLibraryCover(R.drawable.ef_cover_livetv, 0xFF11629F)
			else -> null
		}
	}
}
