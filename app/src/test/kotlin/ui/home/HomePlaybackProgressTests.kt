package org.jellyfin.androidtv.ui.home

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class HomePlaybackProgressTests : FunSpec({
	test("display uses ticks without losing the accepted Aliens resume position") {
		val progress = requireNotNull(homePlaybackProgress(18_993_870_000, 93_000_000_000))
		progress.positionMillis shouldBe 1_899_387
		progress.remainingMillis shouldBe 7_400_613
	}
	test("missing invalid and completed progress does not advertise resume") {
		listOf(null, -1L, 0L, 100L, 101L).forEach { homePlaybackProgress(it, 100) shouldBe null }
		listOf(null, -1L, 0L).forEach { homePlaybackProgress(1, it) shouldBe null }
	}
	test("large runtime values do not overflow progress calculation") {
		val progress = requireNotNull(homePlaybackProgress(Long.MAX_VALUE / 2, Long.MAX_VALUE))
		progress.fraction shouldBe 0.5f
		progress.remainingMillis shouldBe (Long.MAX_VALUE - Long.MAX_VALUE / 2) / 10_000
	}
})
