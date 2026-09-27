package org.jellyfin.androidtv.ui.home

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.UserItemDataDto
import java.util.UUID

class HomeEpisodeLabelTests : FunSpec({
	val episode = BaseItemDto(id = UUID(0, 1), type = BaseItemKind.EPISODE,
		seriesName = "The IT Crowd", parentIndexNumber = 3, indexNumber = 3, name = "Are We Not Men?")
	test("resumable episodes retain their episode identity") {
		val savedProgress = mockk<UserItemDataDto>(relaxed = true) {
			every { playbackPositionTicks } returns 600_000_000L
		}
		val resuming = episode.copy(userData = savedProgress, runTimeTicks = 12_000_000_000L)
		requireNotNull(resuming.homePlaybackProgress())
		resuming.homeEpisodeLabel() shouldBe
			"S3:E3 - Are We Not Men?"
	}
	test("specials and partial numbering remain useful without null text") {
		episode.copy(parentIndexNumber = 0).homeEpisodeLabel() shouldBe "S0:E3 - Are We Not Men?"
		episode.copy(parentIndexNumber = null).homeEpisodeLabel() shouldBe "E3 - Are We Not Men?"
		episode.copy(parentIndexNumber = null, indexNumber = null).homeEpisodeLabel() shouldBe "Are We Not Men?"
		episode.copy(name = " ").homeEpisodeLabel() shouldBe "S3:E3"
	}
	test("non episodes do not replace normal card metadata") {
		episode.copy(type = BaseItemKind.MOVIE).homeEpisodeLabel() shouldBe null
	}
})
