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
})
