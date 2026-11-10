package org.jellyfin.androidtv.ui.playback

import android.view.KeyEvent
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

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

	test("remote D-pad gives direct transport controls") {
		trailerPlaybackCommand(KeyEvent.KEYCODE_DPAD_LEFT) shouldBe "seekBy(-10)"
		trailerPlaybackCommand(KeyEvent.KEYCODE_DPAD_RIGHT) shouldBe "seekBy(10)"
		trailerPlaybackCommand(KeyEvent.KEYCODE_DPAD_CENTER) shouldBe "togglePlayback()"
		trailerPlaybackCommand(KeyEvent.KEYCODE_ENTER) shouldBe "togglePlayback()"
	}

	test("Up toggles captions") {
		trailerPlaybackCommand(KeyEvent.KEYCODE_DPAD_UP) shouldBe "toggleCaptions()"
	}

	test("every D-pad key is consumed so focus never enters the YouTube iframe") {
		listOf(
			KeyEvent.KEYCODE_DPAD_UP,
			KeyEvent.KEYCODE_DPAD_DOWN,
			KeyEvent.KEYCODE_DPAD_LEFT,
			KeyEvent.KEYCODE_DPAD_RIGHT,
			KeyEvent.KEYCODE_DPAD_CENTER,
		).forEach { keyCode ->
			trailerConsumesKey(keyCode) shouldBe true
		}
		trailerPlaybackCommand(KeyEvent.KEYCODE_DPAD_DOWN) shouldBe null
	}

	test("Back remains available to the activity") {
		trailerPlaybackCommand(KeyEvent.KEYCODE_BACK) shouldBe null
		trailerConsumesKey(KeyEvent.KEYCODE_BACK) shouldBe false
	}

	test("captions toggle result maps to the right toast") {
		// WebView.evaluateJavascript wraps string results in quotes; fromResult strips them.
		CaptionsToast.fromResult("\"on\"") shouldBe CaptionsToast.ON
		CaptionsToast.fromResult("\"off\"") shouldBe CaptionsToast.OFF
		CaptionsToast.fromResult("on") shouldBe CaptionsToast.ON
		CaptionsToast.fromResult("off") shouldBe CaptionsToast.OFF
	}

	test("trailers without caption tracks never show the captions-on toast") {
		// The JS toggleCaptions() returns "unavailable" when no caption track exists. This
		// must never map to ON, otherwise the misleading "Captions on" toast fires.
		CaptionsToast.fromResult("\"unavailable\"") shouldBe CaptionsToast.UNAVAILABLE
		CaptionsToast.fromResult("unavailable") shouldBe CaptionsToast.UNAVAILABLE
		CaptionsToast.fromResult(null) shouldBe CaptionsToast.UNAVAILABLE
		CaptionsToast.fromResult("") shouldBe CaptionsToast.UNAVAILABLE
		CaptionsToast.fromResult("unavailable") shouldNotBe CaptionsToast.ON
		CaptionsToast.fromResult(null) shouldNotBe CaptionsToast.ON
	}
})
