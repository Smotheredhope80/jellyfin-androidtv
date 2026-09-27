package org.jellyfin.androidtv.ui.itemdetail

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.NameGuidPair
import java.util.UUID

class ParentArtistTests : FunSpec({
	val albumArtist = NameGuidPair(name = "Album artist", id = UUID(0, 2))
	val trackArtist = NameGuidPair(name = "Track artist", id = UUID(0, 3))
	val album = BaseItemDto(id = UUID(0, 1), type = BaseItemKind.MUSIC_ALBUM,
		albumArtists = listOf(albumArtist), artistItems = listOf(trackArtist))
	test("album artist takes precedence over a track performer") {
		album.primaryArtistId() shouldBe albumArtist.id
	}
	test("missing or empty album artist IDs fall back to artist metadata") {
		album.copy(albumArtists = null).primaryArtistId() shouldBe trackArtist.id
		album.copy(albumArtists = listOf(NameGuidPair(id = UUID(0, 0)))).primaryArtistId() shouldBe trackArtist.id
	}
	test("missing artists and unrelated items do not invent a destination") {
		album.copy(albumArtists = null, artistItems = null).primaryArtistId() shouldBe null
		album.copy(type = BaseItemKind.PLAYLIST).primaryArtistId() shouldBe null
	}
})
