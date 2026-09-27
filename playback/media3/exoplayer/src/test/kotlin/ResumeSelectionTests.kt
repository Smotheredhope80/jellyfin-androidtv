package org.jellyfin.playback.media3.exoplayer

import androidx.media3.common.Player
import io.kotest.core.spec.style.FunSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class ResumeSelectionTests : FunSpec({
	for (target in listOf(0, 1, 4)) {
		test("resume selects target index $target and its timestamp together") {
			val player = mockk<Player>(relaxed = true)
			every { player.currentMediaItemIndex } returns 0
			selectPreparedMediaItem(player, target, 42.minutes)
			verify(exactly = 1) { player.seekTo(target, 2_520_000L) }
			verify(exactly = 0) { player.seekTo(any<Long>()) }
			verify(exactly = 0) { player.seekToNextMediaItem() }
		}
	}

	test("explicit beginning resets an already selected item") {
		val player = mockk<Player>(relaxed = true)
		every { player.currentMediaItemIndex } returns 0
		selectPreparedMediaItem(player, 0, Duration.ZERO)
		verify(exactly = 1) { player.seekTo(0, 0L) }
	}

	test("ordinary next item keeps existing playlist navigation") {
		val player = mockk<Player>(relaxed = true)
		every { player.currentMediaItemIndex } returns 0
		selectPreparedMediaItem(player, 1, null)
		verify(exactly = 1) { player.seekToNextMediaItem() }
		verify(exactly = 0) { player.seekTo(any<Int>(), any<Long>()) }
	}
})
