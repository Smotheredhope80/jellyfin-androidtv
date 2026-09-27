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
