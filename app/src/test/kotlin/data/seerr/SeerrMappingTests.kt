package org.jellyfin.androidtv.data.seerr

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jellyfin.androidtv.ui.seerr.SeerrRowState
import java.util.UUID

// Fixtures are hand-written to match the Seerr API shape; they are not captured server output.
class SeerrMappingTests : FunSpec({
	val json = SeerrClient.json

	test("discover page maps movies, shows and skips people") {
		val page = json.decodeFromString<SeerrPage<SeerrSearchResult>>(
			"""
			{"page":1,"totalPages":3,"totalResults":60,"results":[
			  {"id":11,"mediaType":"movie","title":"Example Film","releaseDate":"2024-05-01","posterPath":"/a.jpg","unknownField":true},
			  {"id":22,"mediaType":"tv","name":"Example Show","firstAirDate":"2019-09-09","mediaInfo":{"status":5}},
			  {"id":33,"mediaType":"person","name":"Some Actor"}
			]}
			""".trimIndent()
		)

		val cards = page.results.mapNotNull { it.toCard() }
		cards.map { it.title } shouldContainExactly listOf("Example Film", "Example Show")
		cards[0].year shouldBe "2024"
		cards[0].availability shouldBe SeerrAvailability.NONE
		cards[1].type shouldBe SeerrMediaType.TV
		cards[1].availability shouldBe SeerrAvailability.AVAILABLE
	}

	test("results without a media type use the row's type") {
		val result = SeerrSearchResult(id = 5, title = "Untyped Film")
		result.toCard(SeerrMediaType.MOVIE)?.type shouldBe SeerrMediaType.MOVIE
		result.toCard() shouldBe null
	}

	test("media status values map to availability") {
		seerrAvailability(SeerrMediaStatus.UNKNOWN) shouldBe SeerrAvailability.NONE
		seerrAvailability(SeerrMediaStatus.PENDING) shouldBe SeerrAvailability.PENDING
		seerrAvailability(SeerrMediaStatus.PROCESSING) shouldBe SeerrAvailability.PROCESSING
		seerrAvailability(SeerrMediaStatus.PARTIALLY_AVAILABLE) shouldBe SeerrAvailability.PARTIAL
		seerrAvailability(SeerrMediaStatus.AVAILABLE) shouldBe SeerrAvailability.AVAILABLE
		seerrAvailability(SeerrMediaStatus.DELETED) shouldBe SeerrAvailability.NONE
		seerrAvailability(null) shouldBe SeerrAvailability.NONE
	}

	test("request status values map to outcomes") {
		seerrRequestOutcome(SeerrRequestStatus.PENDING_APPROVAL) shouldBe SeerrRequestOutcome.WAITING_FOR_APPROVAL
		seerrRequestOutcome(SeerrRequestStatus.APPROVED) shouldBe SeerrRequestOutcome.APPROVED
		seerrRequestOutcome(SeerrRequestStatus.DECLINED) shouldBe SeerrRequestOutcome.DECLINED
	}

	test("movie details keep the Jellyfin item id when present") {
		val itemId = UUID.fromString("0f8e2d43-6c1a-4b7e-9f0d-2a5b3c4d5e6f")
		val title = json.decodeFromString<SeerrMovieDetails>(
			"""{"id":11,"title":"Example Film","releaseDate":"2024-05-01","runtime":118,
			"mediaInfo":{"status":5,"jellyfinMediaId":"${itemId.toString().replace("-", "")}"}}"""
		).toTitle()

		title.jellyfinItemId shouldBe itemId
		title.runtimeMinutes shouldBe 118
		title.canRequest shouldBe false
	}

	test("movie without media info can be requested and has no Jellyfin link") {
		val title = json.decodeFromString<SeerrMovieDetails>("""{"id":12,"title":"New Film"}""").toTitle()
		title.jellyfinItemId shouldBe null
		title.availability shouldBe SeerrAvailability.NONE
		title.canRequest shouldBe true
	}

	test("season choices hide specials and use season and request status") {
		val details = json.decodeFromString<SeerrTvDetails>(
			"""
			{"id":22,"name":"Example Show","seasons":[
			  {"seasonNumber":0,"episodeCount":3,"name":"Specials"},
			  {"seasonNumber":3,"episodeCount":10,"name":"Season 3"},
			  {"seasonNumber":1,"episodeCount":8,"name":"Season 1"},
			  {"seasonNumber":2,"episodeCount":9,"name":""}
			],
			"mediaInfo":{"status":4,
			  "seasons":[{"seasonNumber":1,"status":5}],
			  "requests":[
			    {"id":7,"status":2,"seasons":[{"seasonNumber":2,"status":2}]},
			    {"id":8,"status":3,"seasons":[{"seasonNumber":3,"status":1}]}
			  ]}}
			""".trimIndent()
		)

		val seasons = seasonChoices(details)
		seasons.map { it.number } shouldContainExactly listOf(1, 2, 3)
		seasons.map { it.availability } shouldContainExactly listOf(
			SeerrAvailability.AVAILABLE,
			SeerrAvailability.PENDING,
			// A declined request does not block asking again
			SeerrAvailability.NONE,
		)
		seasons[1].name shouldBe "Season 2"
		details.toTitle().canRequest shouldBe true
	}

	test("show with every season available cannot be requested") {
		val details = SeerrTvDetails(
			id = 1,
			seasons = listOf(SeerrSeason(1, 6)),
			mediaInfo = SeerrMediaInfo(status = 5, seasons = listOf(SeerrSeasonStatus(1, 5))),
		)
		details.toTitle().canRequest shouldBe false
	}

	test("request body for a movie sends only type and id") {
		val body = Json.parseToJsonElement(seerrRequestBody(SeerrMediaType.MOVIE, 11)).jsonObject
		body.keys shouldContainExactly setOf("mediaType", "mediaId")
		body.getValue("mediaType").jsonPrimitive.content shouldBe "movie"
		body.getValue("mediaId").jsonPrimitive.content shouldBe "11"
	}

	test("request body for a show sends sorted seasons") {
		val body = Json.parseToJsonElement(seerrRequestBody(SeerrMediaType.TV, 22, setOf(3, 1))).jsonObject
		body.keys shouldContainExactly setOf("mediaType", "mediaId", "seasons")
		body.getValue("seasons").jsonArray.map { it.jsonPrimitive.content } shouldContainExactly listOf("1", "3")
	}

	test("a show request without seasons is rejected") {
		shouldThrow<IllegalArgumentException> { seerrRequestBody(SeerrMediaType.TV, 22) }
	}

	test("server addresses are normalised") {
		normalizeSeerrUrl("192.168.1.10:5055") shouldBe "http://192.168.1.10:5055"
		normalizeSeerrUrl(" https://seerr.example.com/ ") shouldBe "https://seerr.example.com"
		normalizeSeerrUrl("http://host:5055/api/v1/") shouldBe "http://host:5055"
		normalizeSeerrUrl("") shouldBe null
		normalizeSeerrUrl("http://") shouldBe null
		normalizeSeerrUrl("http://bad host") shouldBe null
	}

	test("only the session cookie name and value are kept") {
		seerrSessionCookie(
			listOf(
				"other=1; Path=/",
				"connect.sid=s%3Aabc.def; Path=/; Expires=Wed, 26 Nov 2026 10:00:00 GMT; HttpOnly",
			)
		) shouldBe "connect.sid=s%3Aabc.def"
		seerrSessionCookie(listOf("connect.sid=; Path=/")) shouldBe null
		seerrSessionCookie(emptyList()) shouldBe null
	}

	test("TMDB image urls need a rooted path") {
		tmdbImageUrl("/p.jpg", "w342") shouldBe "https://image.tmdb.org/t/p/w342/p.jpg"
		tmdbImageUrl(null, "w342") shouldBe null
		tmdbImageUrl("https://elsewhere/p.jpg", "w342") shouldBe null
	}

	test("user label prefers display name") {
		SeerrUser(id = 3, displayName = "Gary", username = "gary2").label shouldBe "Gary"
		SeerrUser(id = 3, displayName = " ", jellyfinUsername = "gary").label shouldBe "gary"
		SeerrUser(id = 3).label shouldBe "#3"
	}

	test("row paging drops cards repeated across pages") {
		fun card(id: Int) = SeerrCard(SeerrMediaType.MOVIE, id, "Film $id", null, null, SeerrAvailability.NONE)
		val first = SeerrRowState().append(SeerrCardPage(listOf(card(1), card(2)), page = 1, totalPages = 2))
		first.canLoadMore shouldBe true
		val second = first.append(SeerrCardPage(listOf(card(2), card(3)), page = 2, totalPages = 2))
		second.cards.map { it.tmdbId } shouldContainExactly listOf(1, 2, 3)
		second.canLoadMore shouldBe false
	}
})
