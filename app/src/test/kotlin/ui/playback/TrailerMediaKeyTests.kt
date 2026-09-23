package org.jellyfin.androidtv.ui.playback

import android.view.KeyEvent
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TrailerMediaKeyTests : FunSpec({
	test("dedicated play and pause have distinct commands") {
		trailerPlaybackCommand(
			KeyEvent.KEYCODE_MEDIA_PLAY,
		) shouldBe "playPlayback()"

		trailerPlaybackCommand(
			KeyEvent.KEYCODE_MEDIA_PAUSE,
		) shouldBe "pausePlayback()"

		trailerPlaybackCommand(
			KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
		) shouldBe "togglePlayback()"
	}

	test("transport seek commands use ten second steps") {
		trailerPlaybackCommand(
			KeyEvent.KEYCODE_MEDIA_REWIND,
		) shouldBe "seekBy(-10)"

		trailerPlaybackCommand(
			KeyEvent.KEYCODE_MEDIA_FAST_FORWARD,
		) shouldBe "seekBy(10)"
	}

	test("navigation keys remain available to WebView and activity") {
		listOf(
			KeyEvent.KEYCODE_DPAD_UP,
			KeyEvent.KEYCODE_DPAD_DOWN,
			KeyEvent.KEYCODE_DPAD_LEFT,
			KeyEvent.KEYCODE_DPAD_RIGHT,
			KeyEvent.KEYCODE_DPAD_CENTER,
			KeyEvent.KEYCODE_ENTER,
			KeyEvent.KEYCODE_BACK,
		).forEach { keyCode ->
			trailerPlaybackCommand(keyCode) shouldBe null
		}
	}
})
