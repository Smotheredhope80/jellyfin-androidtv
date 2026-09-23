package org.jellyfin.androidtv.ui.home

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.ui.itemdetail.ThemeMusicGate
import org.jellyfin.androidtv.ui.itemdetail.supportsThemeMusic
import org.jellyfin.androidtv.util.seriesLogoFallbackId
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.CollectionType
import java.util.UUID

class TvPresentationTests : FunSpec({
	val id = UUID(0, 1)
	val series = UUID(0, 2)
	test("TV children inherit series logos but unrelated items do not") {
		for (type in listOf(BaseItemKind.EPISODE, BaseItemKind.SEASON)) {
			BaseItemDto(id = id, type = type, seriesId = series).seriesLogoFallbackId() shouldBe series
			BaseItemDto(id = id, type = type).seriesLogoFallbackId() shouldBe null
		}
		BaseItemDto(id = id, type = BaseItemKind.MOVIE, seriesId = series).seriesLogoFallbackId() shouldBe null
	}
	test("theme eligibility is restricted to series and season pages") {
		for (type in BaseItemKind.entries) {
			BaseItemDto(id = id, type = type).supportsThemeMusic() shouldBe (type == BaseItemKind.SERIES || type == BaseItemKind.SEASON)
		}
	}
	test("leaving a page invalidates a delayed theme response or prepared callback") {
		val gate = ThemeMusicGate()
		val old = gate.begin()
		gate.accepts(old) shouldBe true
		gate.stop()
		gate.accepts(old) shouldBe false
		val next = gate.begin()
		gate.accepts(old) shouldBe false
		gate.accepts(next) shouldBe true
	}
	test("Playlists artwork applies to the library but not an individual playlist") {
		BaseItemDto(id = id, type = BaseItemKind.USER_VIEW, collectionType = CollectionType.PLAYLISTS)
			.libraryArtwork() shouldBe R.drawable.library_playlists
		BaseItemDto(id = id, type = BaseItemKind.PLAYLIST, name = "Playlists").libraryArtwork() shouldBe null
	}
})
