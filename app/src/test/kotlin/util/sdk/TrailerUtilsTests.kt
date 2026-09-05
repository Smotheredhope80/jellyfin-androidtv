package org.jellyfin.androidtv.util.sdk

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TrailerUtilsTests : FunSpec({
	test("extracts YouTube ids from supported remote trailer formats") {
		val id = "dQw4w9WgXcQ"
		TrailerUtils.getYoutubeVideoId("https://www.youtube.com/watch?v=$id") shouldBe id
		TrailerUtils.getYoutubeVideoId("https://youtu.be/$id") shouldBe id
		TrailerUtils.getYoutubeVideoId("https://m.youtube.com/embed/$id") shouldBe id
		TrailerUtils.getYoutubeVideoId("https://youtube.com/shorts/$id") shouldBe id
		TrailerUtils.getYoutubeVideoId("plugin://plugin.video.youtube/?action=play_video&videoid=$id") shouldBe id
	}

	test("rejects non-YouTube and malformed trailer ids") {
		TrailerUtils.getYoutubeVideoId("https://vimeo.com/123456") shouldBe null
		TrailerUtils.getYoutubeVideoId("https://notyoutube.com/watch?v=dQw4w9WgXcQ") shouldBe null
		TrailerUtils.getYoutubeVideoId("https://youtube.com/watch?v=too-short") shouldBe null
	}
})
