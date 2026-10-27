package org.jellyfin.androidtv.data.seerr

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

// Fixtures are hand-written to match Seerr v3.5.1's response shapes; they are not captured server output.
class SeerrPhase2MappingTests : FunSpec({
	val json = SeerrClient.json

	fun video(key: String?, type: String?, site: String? = "YouTube") = SeerrVideo(site = site, key = key, type = type)

	test("trailer pick prefers trailers, then teasers, keeping server order") {
		pickTrailerKey(
			listOf(
				video("AAAAAAAAAAA", "Teaser"),
				video("BBBBBBBBBBB", "Trailer", site = "Vimeo"),
				video("CCCCCCCCCCC", "Trailer"),
				video("DDDDDDDDDDD", "Trailer"),
			)
		) shouldBe "CCCCCCCCCCC"
		pickTrailerKey(listOf(video("EEEEEEEEEEE", "Clip"), video("FFFFFFFFFFF", "Teaser"))) shouldBe "FFFFFFFFFFF"
		pickTrailerKey(listOf(video("GGGGGGGGGGG", "Featurette"))) shouldBe "GGGGGGGGGGG"
	}

	test("trailer pick ignores invalid keys and other sites") {
		pickTrailerKey(listOf(video("short", "Trailer"), video(null, "Trailer"), video("HHHHHHHHHHH", "Trailer", site = "Vimeo"))) shouldBe null
		pickTrailerKey(emptyList()) shouldBe null
	}

	test("film certification uses the region, then the US, preferring theatrical") {
		val releases = SeerrReleases(
			listOf(
				SeerrReleaseCountry("GB", listOf(SeerrReleaseDate("", 3), SeerrReleaseDate("15", 4), SeerrReleaseDate("12A", 3))),
				SeerrReleaseCountry("US", listOf(SeerrReleaseDate("PG-13", 3))),
				SeerrReleaseCountry("DE", listOf(SeerrReleaseDate(" ", 3))),
			)
		)
		movieCertification(releases, "gb") shouldBe "12A"
		movieCertification(releases, "FR") shouldBe "PG-13"
		movieCertification(releases, "DE") shouldBe "PG-13"
		movieCertification(releases, "") shouldBe "PG-13"
		movieCertification(null, "GB") shouldBe null
	}

	test("film certification falls back to any non-theatrical rating") {
		movieCertification(SeerrReleases(listOf(SeerrReleaseCountry("US", listOf(SeerrReleaseDate("R", 4))))), "US") shouldBe "R"
	}

	test("show content rating uses the region, then the US") {
		val ratings = SeerrContentRatings(listOf(SeerrContentRating("US", "TV-14"), SeerrContentRating("GB", "15")))
		tvContentRating(ratings, "GB") shouldBe "15"
		tvContentRating(ratings, "AU") shouldBe "TV-14"
		tvContentRating(SeerrContentRatings(listOf(SeerrContentRating("GB", ""))), "GB") shouldBe null
	}

	test("money is shown compactly in USD and unknown amounts are hidden") {
		formatUsdCompact(null) shouldBe null
		formatUsdCompact(0) shouldBe null
		formatUsdCompact(-5) shouldBe null
		formatUsdCompact(500) shouldBe "$500"
		formatUsdCompact(950_000) shouldBe "$950K"
		formatUsdCompact(12_500_000) shouldBe "$12.5M"
		formatUsdCompact(200_000_000) shouldBe "$200M"
		formatUsdCompact(1_234_567_890) shouldBe "$1.2B"
		formatUsdCompact(2_000_000_000) shouldBe "$2B"
		formatUsdCompact(999_600) shouldBe "$1M"
	}

	test("free space is shown in GB or TB") {
		formatFreeSpace(850_000_000_000) shouldBe "850 GB"
		formatFreeSpace(1_234_000_000_000) shouldBe "1.2 TB"
		formatFreeSpace(null) shouldBe null
	}

	test("anime is detected from TMDB's anime keyword") {
		isAnime(listOf(SeerrNamed(1, "based on manga"), SeerrNamed(SEERR_ANIME_KEYWORD_ID, "anime"))) shouldBe true
		isAnime(listOf(SeerrNamed(1, "anime"))) shouldBe false
		isAnime(emptyList()) shouldBe false
	}

	test("advanced requests need admin, manage requests or advanced requests") {
		hasAdvancedRequestPermission(SeerrPermission.ADMIN) shouldBe true
		hasAdvancedRequestPermission(SeerrPermission.MANAGE_REQUESTS) shouldBe true
		hasAdvancedRequestPermission(SeerrPermission.REQUEST_ADVANCED or 32L) shouldBe true
		// REQUEST, AUTO_APPROVE and REQUEST_4K alone are not enough
		hasAdvancedRequestPermission(32L or 128L or 1024L) shouldBe false
		hasAdvancedRequestPermission(0) shouldBe false
	}

	test("servers offered exclude 4K and put the default first") {
		val servers = listOf(
			SeerrServiceServer(id = 0, name = "Main"),
			SeerrServiceServer(id = 1, name = "4K", is4k = true, isDefault = true),
			SeerrServiceServer(id = 2, name = "Default", isDefault = true),
		)
		filterRequestServers(servers).map { it.id } shouldContainExactly listOf(2, 0)
		defaultServerId(servers) shouldBe 2
		defaultServerId(listOf(SeerrServiceServer(id = 0), SeerrServiceServer(id = 1, is4k = true, isDefault = true))) shouldBe null
	}

	val sonarr = SeerrServiceDetails(
		server = SeerrServiceServer(
			id = 3,
			activeProfileId = 4,
			activeDirectory = "/tv",
			activeLanguageProfileId = 1,
			activeAnimeProfileId = 7,
			activeAnimeDirectory = "/anime",
			activeAnimeLanguageProfileId = 2,
			activeTags = listOf(10),
			activeAnimeTags = listOf(11, 12),
		),
	)

	test("defaults follow the server, with anime values for anime shows") {
		defaultRequestOptions(sonarr, anime = false) shouldBe SeerrRequestOptions(3, 4, "/tv", 1, setOf(10))
		defaultRequestOptions(sonarr, anime = true) shouldBe SeerrRequestOptions(3, 7, "/anime", 2, setOf(11, 12))
	}

	test("anime without anime settings uses normal values, except tags which Seerr leaves empty") {
		val plain = SeerrServiceDetails(server = SeerrServiceServer(id = 3, activeProfileId = 4, activeDirectory = "/tv", activeAnimeDirectory = " ", activeTags = listOf(10)))
		defaultRequestOptions(plain, anime = true) shouldBe SeerrRequestOptions(3, 4, "/tv", null, emptySet())
	}

	val defaults = SeerrRequestOptions(serverId = 3, profileId = 4, rootFolder = "/tv", languageProfileId = 1, tags = setOf(10))

	fun body(selected: SeerrRequestOptions, defaultServerId: Int? = 3) = Json.parseToJsonElement(
		seerrRequestBody(SeerrMediaType.TV, 22, setOf(2, 1), SeerrAdvancedRequest(selected, defaults, defaultServerId))
	).jsonObject

	test("unchanged advanced options send exactly the phase 1 body") {
		seerrRequestBody(SeerrMediaType.TV, 22, setOf(2, 1), SeerrAdvancedRequest(defaults, defaults, 3)) shouldBe
			seerrRequestBody(SeerrMediaType.TV, 22, setOf(2, 1))
		seerrRequestBody(SeerrMediaType.MOVIE, 11, advanced = SeerrAdvancedRequest(defaults, defaults, 3)) shouldBe
			seerrRequestBody(SeerrMediaType.MOVIE, 11)
	}

	test("only changed options are added to the body") {
		val profile = body(defaults.copy(profileId = 9))
		profile.keys shouldContainExactly setOf("mediaType", "mediaId", "seasons", "profileId")
		profile.getValue("profileId").jsonPrimitive.content shouldBe "9"

		body(defaults.copy(rootFolder = "/tv2")).keys shouldContainExactly setOf("mediaType", "mediaId", "seasons", "rootFolder")
		body(defaults.copy(languageProfileId = 2)).keys shouldContainExactly setOf("mediaType", "mediaId", "seasons", "languageProfileId")

		val tags = body(defaults.copy(tags = setOf(12, 10)))
		tags.keys shouldContainExactly setOf("mediaType", "mediaId", "seasons", "tags")
		tags.getValue("tags").jsonArray.map { it.jsonPrimitive.content } shouldContainExactly listOf("10", "12")
	}

	test("clearing every tag sends an empty tag list") {
		body(defaults.copy(tags = emptySet())).getValue("tags").jsonArray.size shouldBe 0
	}

	test("the server is sent only when it is not Seerr's default") {
		val other = SeerrRequestOptions(serverId = 5, profileId = 4, rootFolder = "/tv", languageProfileId = 1, tags = setOf(10))
		val otherDefaults = other
		val chosen = Json.parseToJsonElement(
			seerrRequestBody(SeerrMediaType.TV, 22, setOf(1), SeerrAdvancedRequest(other, otherDefaults, defaultServerId = 3))
		).jsonObject
		chosen.keys shouldContainExactly setOf("mediaType", "mediaId", "seasons", "serverId")
		chosen.getValue("serverId").jsonPrimitive.content shouldBe "5"

		// With no default server marked, Seerr has no fallback, so the server is always sent
		body(defaults, defaultServerId = null).keys shouldContainExactly setOf("mediaType", "mediaId", "seasons", "serverId")
	}

	test("advanced bodies never carry 4K, user or quota fields") {
		val changed = body(SeerrRequestOptions(serverId = 3, profileId = 9, rootFolder = "/x", languageProfileId = 2, tags = setOf(1)), defaultServerId = null)
		changed.keys.intersect(setOf("is4k", "userId", "ignoreQuota")) shouldBe emptySet()
	}

	test("defaults from another server are rejected") {
		shouldThrow<IllegalArgumentException> {
			seerrRequestBody(SeerrMediaType.MOVIE, 11, advanced = SeerrAdvancedRequest(defaults.copy(serverId = 5), defaults, 3))
		}
	}

	test("service details parse fields outside the OpenAPI file") {
		val details = json.decodeFromString<SeerrServiceDetails>(
			"""
			{"server":{"id":0,"name":"Radarr","is4k":false,"isDefault":true,"activeDirectory":"/movies","activeProfileId":6,"activeTags":[2]},
			 "profiles":[{"id":6,"name":"HD-1080p"},{"id":7,"name":"Ultra-HD"}],
			 "rootFolders":[{"id":1,"freeSpace":1200000000000,"path":"/movies","totalSpace":4000000000000}],
			 "languageProfiles":null,
			 "tags":[{"id":2,"label":"gary"},{"id":3,"label":"kids"}]}
			""".trimIndent()
		)
		details.profiles.map { it.name } shouldContainExactly listOf("HD-1080p", "Ultra-HD")
		details.rootFolders.single().freeSpace shouldBe 1_200_000_000_000
		details.languageProfiles shouldBe null
		details.tags.map { it.label } shouldContainExactly listOf("gary", "kids")
		defaultRequestOptions(details, anime = false) shouldBe SeerrRequestOptions(0, 6, "/movies", null, setOf(2))
	}

	test("a Radarr server without tags has no default tags") {
		val details = json.decodeFromString<SeerrServiceDetails>("""{"server":{"id":0,"activeProfileId":6,"activeDirectory":"/movies"}}""")
		defaultRequestOptions(details, anime = false).tags shouldBe emptySet()
	}

	test("combined ratings tolerate missing parts and odd scores") {
		json.decodeFromString<SeerrCombinedRatings>("""{"rt":{"criticsScore":85,"criticsRating":"Certified Fresh","audienceScore":64.6}}""").toRatings() shouldBe
			SeerrRatings(rtCritics = 85, rtCriticsRating = "Certified Fresh", rtAudience = 65, imdb = null)
		json.decodeFromString<SeerrCombinedRatings>("""{"imdb":{"title":"x","criticsScore":6.5}}""").toRatings() shouldBe SeerrRatings(imdb = 6.5)
		json.decodeFromString<SeerrCombinedRatings>("""{"rt":{"criticsScore":140}}""").toRatings().isEmpty shouldBe true
		json.decodeFromString<SeerrRtRating>("""{"title":"Show","criticsScore":91,"criticsRating":"Fresh"}""").toRatings().rtCritics shouldBe 91
	}

	test("film details fill the info section") {
		val title = json.decodeFromString<SeerrMovieDetails>(
			"""
			{"id":11,"title":"Example Film","releaseDate":"2024-05-01","tagline":" A tagline ","status":"Released",
			 "genres":[{"id":1,"name":"Action"},{"id":2,"name":"Drama"},{"id":3,"name":"War"},{"id":4,"name":"History"}],
			 "relatedVideos":[{"site":"YouTube","key":"AAAAAAAAAAA","type":"Teaser"},{"site":"YouTube","key":"BBBBBBBBBBB","type":"Trailer"}],
			 "budget":200000000,"revenue":0,"voteAverage":7.24,"voteCount":1200,
			 "productionCompanies":[{"id":1,"name":"Studio A"},{"id":2,"name":"Studio B"},{"id":3,"name":"Studio C"}],
			 "credits":{"cast":[{"name":"Second","order":1},{"name":"First","order":0}],
			            "crew":[{"name":"Writer","job":"Screenplay"},{"name":"The Director","job":"Director"}]},
			 "releases":{"results":[{"iso_3166_1":"GB","release_dates":[{"certification":"15","type":3}]}]}}
			""".trimIndent()
		).toTitle(region = "GB")

		title.trailerKey shouldBe "BBBBBBBBBBB"
		title.tmdbScore shouldBe 7.24
		title.info.tagline shouldBe "A tagline"
		title.info.certification shouldBe "15"
		title.info.genres shouldContainExactly listOf("Action", "Drama", "War")
		title.info.budget shouldBe "$200M"
		title.info.revenue shouldBe null
		title.info.credit shouldContainExactly listOf("The Director")
		title.info.companies shouldContainExactly listOf("Studio A", "Studio B")
		title.info.cast shouldContainExactly listOf("First", "Second")
		title.anime shouldBe false
	}

	test("a score with no votes is hidden") {
		json.decodeFromString<SeerrMovieDetails>("""{"id":1,"voteAverage":0,"voteCount":0}""").toTitle().tmdbScore shouldBe null
		json.decodeFromString<SeerrMovieDetails>("""{"id":1,"voteAverage":6.0,"voteCount":0}""").toTitle().tmdbScore shouldBe null
	}

	test("show details fill the info section and detect anime") {
		val title = json.decodeFromString<SeerrTvDetails>(
			"""
			{"id":22,"name":"Example Show","firstAirDate":"2019-09-09","lastAirDate":"2024-01-02","status":"Returning Series",
			 "episodeRunTime":[0,45],"nextEpisodeToAir":{"airDate":"2026-11-01"},
			 "networks":[{"id":1,"name":"Network"}],"createdBy":[{"id":5,"name":"Creator"}],
			 "contentRatings":{"results":[{"iso_3166_1":"US","rating":"TV-MA"}]},
			 "keywords":[{"id":210024,"name":"anime"}],
			 "relatedVideos":[],"seasons":[]}
			""".trimIndent()
		).toTitle(region = "GB")

		title.trailerKey shouldBe null
		title.info.certification shouldBe "TV-MA"
		title.info.episodeRuntimeMinutes shouldBe 45
		title.info.nextEpisodeDate shouldBe "2026-11-01"
		title.info.lastAirDate shouldBe "2024-01-02"
		title.info.companies shouldContainExactly listOf("Network")
		title.info.credit shouldContainExactly listOf("Creator")
		title.anime shouldBe true
	}

	test("phase 1 shaped responses still map with empty extras") {
		val title = json.decodeFromString<SeerrMovieDetails>("""{"id":12,"title":"New Film"}""").toTitle()
		title.trailerKey shouldBe null
		title.info shouldBe SeerrInfo()
		json.decodeFromString<SeerrUser>("""{"id":4}""").permissions shouldBe 0
	}
})
