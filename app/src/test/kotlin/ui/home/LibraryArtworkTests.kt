package org.jellyfin.androidtv.ui.home

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.jellyfin.androidtv.R
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.CollectionType

class LibraryArtworkTests : FunSpec({
	test("staging name takes precedence over generic music type with different separators") {
		listOf("Music-Staging", "Music–Staging", " MUSIC — STAGING ", "Music_Staging").forEach {
			BaseItemDto(id = java.util.UUID(0, 1), name = it, type = BaseItemKind.USER_VIEW, collectionType = CollectionType.MUSIC)
				.libraryArtwork() shouldBe R.drawable.library_music_staging
		}
	}
	test("renamed libraries use collection type while unsupported libraries retain server artwork") {
		BaseItemDto(id = java.util.UUID(0, 1), name = "Cinema", type = BaseItemKind.COLLECTION_FOLDER, collectionType = CollectionType.MOVIES)
			.libraryArtwork() shouldBe R.drawable.library_movies
		BaseItemDto(id = java.util.UUID(0, 1), name = "Books", type = BaseItemKind.USER_VIEW, collectionType = CollectionType.BOOKS)
			.libraryArtwork() shouldBe null
	}
	test("individual items with matching names never get library artwork") {
		listOf(BaseItemKind.MOVIE, BaseItemKind.SERIES, BaseItemKind.MUSIC_ALBUM, BaseItemKind.BOX_SET).forEach {
			BaseItemDto(id = java.util.UUID(0, 1), name = "Collections", type = it).libraryArtwork() shouldBe null
		}
	}
	test("elegantFinMediaCover maps all 8 supported library types and colors") {
		val id = java.util.UUID(0, 1)
		BaseItemDto(id = id, name = "Movies", type = BaseItemKind.USER_VIEW, collectionType = CollectionType.MOVIES)
			.elegantFinMediaCover() shouldBe ElegantFinLibraryCover(R.drawable.ef_cover_movies, 0xFFC16768)
		BaseItemDto(id = id, name = "TV Shows", type = BaseItemKind.USER_VIEW, collectionType = CollectionType.TVSHOWS)
			.elegantFinMediaCover() shouldBe ElegantFinLibraryCover(R.drawable.ef_cover_tvshows, 0xFF8C952B)
		BaseItemDto(id = id, name = "Music-Staging", type = BaseItemKind.USER_VIEW, collectionType = CollectionType.MUSIC)
			.elegantFinMediaCover() shouldBe ElegantFinLibraryCover(R.drawable.ef_cover_folders, 0xFFAD3C71)
		BaseItemDto(id = id, name = "Music Videos", type = BaseItemKind.USER_VIEW, collectionType = CollectionType.MUSICVIDEOS)
			.elegantFinMediaCover() shouldBe ElegantFinLibraryCover(R.drawable.ef_cover_homevideos, 0xFF275AB9)
		BaseItemDto(id = id, name = "Music", type = BaseItemKind.USER_VIEW, collectionType = CollectionType.MUSIC)
			.elegantFinMediaCover() shouldBe ElegantFinLibraryCover(R.drawable.ef_cover_music, 0xFF0B5D48)
		BaseItemDto(id = id, name = "Collections", type = BaseItemKind.USER_VIEW, collectionType = CollectionType.BOXSETS)
			.elegantFinMediaCover() shouldBe ElegantFinLibraryCover(R.drawable.ef_cover_boxsets, 0xFFDBB435)
		BaseItemDto(id = id, name = "Playlists", type = BaseItemKind.USER_VIEW, collectionType = CollectionType.PLAYLISTS)
			.elegantFinMediaCover() shouldBe ElegantFinLibraryCover(R.drawable.ef_cover_playlists, 0xFF763DD8)
		BaseItemDto(id = id, name = "Live TV", type = BaseItemKind.USER_VIEW, collectionType = CollectionType.LIVETV)
			.elegantFinMediaCover() shouldBe ElegantFinLibraryCover(R.drawable.ef_cover_livetv, 0xFF11629F)
	}
	test("elegantFinMediaCover falls back to collectionType for renamed folders and returns null for unsupported") {
		val id = java.util.UUID(0, 1)
		BaseItemDto(id = id, name = "Cinema", type = BaseItemKind.COLLECTION_FOLDER, collectionType = CollectionType.MOVIES)
			.elegantFinMediaCover() shouldBe ElegantFinLibraryCover(R.drawable.ef_cover_movies, 0xFFC16768)
		BaseItemDto(id = id, name = "Series Archive", type = BaseItemKind.USER_VIEW, collectionType = CollectionType.TVSHOWS)
			.elegantFinMediaCover() shouldBe ElegantFinLibraryCover(R.drawable.ef_cover_tvshows, 0xFF8C952B)
		BaseItemDto(id = id, name = "Books", type = BaseItemKind.USER_VIEW, collectionType = CollectionType.BOOKS)
			.elegantFinMediaCover() shouldBe null
	}
	test("individual items never receive elegantFinMediaCover") {
		listOf(BaseItemKind.MOVIE, BaseItemKind.SERIES, BaseItemKind.MUSIC_ALBUM, BaseItemKind.BOX_SET).forEach {
			BaseItemDto(id = java.util.UUID(0, 1), name = "Movies", type = it).elegantFinMediaCover() shouldBe null
		}
	}
})
