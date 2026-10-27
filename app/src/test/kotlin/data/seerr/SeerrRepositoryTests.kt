package org.jellyfin.androidtv.data.seerr

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import org.jellyfin.androidtv.auth.repository.UserRepositoryImpl
import org.jellyfin.androidtv.preference.SeerrPreferences
import org.jellyfin.sdk.model.api.UserDto
import java.util.UUID

class SeerrRepositoryTests : FunSpec({
	val ownerId = UUID.fromString("11111111-2222-3333-4444-555555555555")
	val otherId = UUID.fromString("99999999-8888-7777-6666-555555555555")
	val pageBody = """{"page":1,"totalPages":1,"results":[]}"""

	fun preferences(signedIn: Boolean) = mockk<SeerrPreferences>(relaxed = true).also { prefs ->
		every { prefs[SeerrPreferences.serverUrl] } returns "http://seerr.test:5055"
		every { prefs[SeerrPreferences.sessionCookie] } returns if (signedIn) "connect.sid=abc" else ""
		every { prefs[SeerrPreferences.jellyfinUserId] } returns if (signedIn) ownerId.toString() else ""
		every { prefs[SeerrPreferences.seerrUserId] } returns 4
		every { prefs[SeerrPreferences.seerrUserName] } returns "Owner"
	}

	fun user(id: UUID) = mockk<UserDto>().also { every { it.id } returns id }

	test("Discover is unavailable without a session") {
		val users = UserRepositoryImpl().apply { setCurrentUser(user(ownerId)) }
		val repository = SeerrRepository(mockk(), preferences(signedIn = false), users)
		repository.available.first() shouldBe false
	}

	test("Discover is available only to the Jellyfin user who signed in") {
		val users = UserRepositoryImpl().apply { setCurrentUser(user(ownerId)) }
		val repository = SeerrRepository(mockk(), preferences(signedIn = true), users)
		repository.available.first() shouldBe true

		users.setCurrentUser(user(otherId))
		repository.available.first() shouldBe false

		users.setCurrentUser(null)
		repository.available.first() shouldBe false
	}

	test("a rejected call clears the session when /auth/me also rejects") {
		val prefs = preferences(signedIn = true)
		val client = mockk<SeerrClient>()
		coEvery { client.get(any(), "/discover/trending", any(), any()) } throws SeerrHttpException(403, "forbidden")
		coEvery { client.get(any(), "/auth/me", any(), any()) } throws SeerrHttpException(403, "forbidden")
		val repository = SeerrRepository(client, prefs, UserRepositoryImpl())

		shouldThrow<SeerrSignedOutException> { repository.discover(SeerrDiscoverRow.TRENDING, 1) }
		repository.session.value shouldBe null
		verify { prefs.delete(SeerrPreferences.sessionCookie) }
	}

	test("a permission error keeps the session when /auth/me still works") {
		val prefs = preferences(signedIn = true)
		val client = mockk<SeerrClient>()
		coEvery { client.post(any(), "/request", any(), any()) } throws SeerrHttpException(403, "No permission")
		coEvery { client.get(any(), "/auth/me", any(), any()) } returns SeerrResponse("""{"id":4}""", emptyList())
		val repository = SeerrRepository(client, prefs, UserRepositoryImpl())

		val error = shouldThrow<SeerrHttpException> { repository.request(SeerrMediaType.MOVIE, 11, emptySet()) }
		error.message shouldBe "No permission"
		repository.session.value?.cookie shouldBe "connect.sid=abc"
		verify(exactly = 0) { prefs.delete(SeerrPreferences.sessionCookie) }
	}

	test("other server errors pass through without a session check") {
		val client = mockk<SeerrClient>()
		coEvery { client.get(any(), "/search", any(), any()) } throws SeerrHttpException(500, "boom")
		val repository = SeerrRepository(client, preferences(signedIn = true), UserRepositoryImpl())

		shouldThrow<SeerrHttpException> { repository.search("x") }
		coVerify(exactly = 0) { client.get(any(), "/auth/me", any(), any()) }
	}

	test("sign out clears the local session even when the server logout fails") {
		val prefs = preferences(signedIn = true)
		val client = mockk<SeerrClient>()
		coEvery { client.post(any(), "/auth/logout", any(), any()) } throws SeerrHttpException(0, "offline")
		val repository = SeerrRepository(client, prefs, UserRepositoryImpl())

		repository.signOut()
		repository.session.value shouldBe null
		verify { prefs.delete(SeerrPreferences.sessionCookie) }
	}

	test("missing ratings are empty, not an error, and do not check the session") {
		val client = mockk<SeerrClient>()
		coEvery { client.get(any(), "/movie/11/ratingscombined", any(), any()) } throws SeerrHttpException(404, "No ratings found.")
		val repository = SeerrRepository(client, preferences(signedIn = true), UserRepositoryImpl())

		repository.ratings(SeerrMediaType.MOVIE, 11).isEmpty shouldBe true
		coVerify(exactly = 0) { client.get(any(), "/auth/me", any(), any()) }
	}

	test("show ratings use the Rotten Tomatoes endpoint") {
		val client = mockk<SeerrClient>()
		coEvery { client.get(any(), "/tv/22/ratings", any(), any()) } returns SeerrResponse("""{"criticsScore":91}""", emptyList())
		val repository = SeerrRepository(client, preferences(signedIn = true), UserRepositoryImpl())

		repository.ratings(SeerrMediaType.TV, 22).rtCritics shouldBe 91
	}

	test("permissions are read once and re-read after ten minutes") {
		var now = 0L
		val client = mockk<SeerrClient>()
		coEvery { client.get(any(), "/auth/me", any(), any()) } returns SeerrResponse("""{"id":4,"permissions":8224}""", emptyList())
		val repository = SeerrRepository(client, preferences(signedIn = true), UserRepositoryImpl(), region = { "GB" }, clock = { now })

		repository.canRequestAdvanced() shouldBe true
		now += 9 * 60 * 1000L
		repository.canRequestAdvanced() shouldBe true
		coVerify(exactly = 1) { client.get(any(), "/auth/me", any(), any()) }

		now += 2 * 60 * 1000L
		repository.canRequestAdvanced()
		coVerify(exactly = 2) { client.get(any(), "/auth/me", any(), any()) }
	}

	val advanced = SeerrAdvancedRequest(
		selected = SeerrRequestOptions(0, profileId = 7, rootFolder = "/movies", languageProfileId = null, tags = emptySet()),
		defaults = SeerrRequestOptions(0, profileId = 6, rootFolder = "/movies", languageProfileId = null, tags = emptySet()),
		defaultServerId = 0,
	)

	test("advanced options are refused without the permission and nothing is sent") {
		val client = mockk<SeerrClient>()
		coEvery { client.get(any(), "/auth/me", any(), any()) } returns SeerrResponse("""{"id":4,"permissions":32}""", emptyList())
		val repository = SeerrRepository(client, preferences(signedIn = true), UserRepositoryImpl())

		shouldThrow<SeerrAdvancedNotAllowedException> { repository.request(SeerrMediaType.MOVIE, 11, emptySet(), advanced) }
		coVerify(exactly = 0) { client.post(any(), "/request", any(), any()) }
	}

	test("a permission removed in Seerr blocks advanced options at once, despite the cache") {
		val client = mockk<SeerrClient>()
		coEvery { client.get(any(), "/auth/me", any(), any()) } returnsMany listOf(
			SeerrResponse("""{"id":4,"permissions":8192}""", emptyList()),
			SeerrResponse("""{"id":4,"permissions":32}""", emptyList()),
		)
		val repository = SeerrRepository(client, preferences(signedIn = true), UserRepositoryImpl(), clock = { 0L })

		repository.canRequestAdvanced() shouldBe true
		shouldThrow<SeerrAdvancedNotAllowedException> { repository.request(SeerrMediaType.MOVIE, 11, emptySet(), advanced) }
		coVerify(exactly = 2) { client.get(any(), "/auth/me", any(), any()) }
		coVerify(exactly = 0) { client.post(any(), "/request", any(), any()) }
	}

	test("advanced options are sent when allowed") {
		val client = mockk<SeerrClient>()
		coEvery { client.get(any(), "/auth/me", any(), any()) } returns SeerrResponse("""{"id":4,"permissions":8192}""", emptyList())
		coEvery { client.post(any(), "/request", any(), any()) } returns SeerrResponse("""{"id":1,"status":2}""", emptyList())
		val repository = SeerrRepository(client, preferences(signedIn = true), UserRepositoryImpl())

		repository.request(SeerrMediaType.MOVIE, 11, emptySet(), advanced) shouldBe SeerrRequestOutcome.APPROVED
		coVerify { client.post(any(), "/request", any(), """{"mediaType":"movie","mediaId":11,"profileId":7}""") }
	}

	test("a plain request needs no permission check") {
		val client = mockk<SeerrClient>()
		coEvery { client.post(any(), "/request", any(), any()) } returns SeerrResponse("""{"id":1,"status":1}""", emptyList())
		val repository = SeerrRepository(client, preferences(signedIn = true), UserRepositoryImpl())

		repository.request(SeerrMediaType.MOVIE, 11, emptySet()) shouldBe SeerrRequestOutcome.WAITING_FOR_APPROVAL
		coVerify(exactly = 0) { client.get(any(), "/auth/me", any(), any()) }
	}

	test("server details are cached for five minutes") {
		var now = 0L
		val client = mockk<SeerrClient>()
		coEvery { client.get(any(), "/service/sonarr/3", any(), any()) } returns SeerrResponse("""{"server":{"id":3}}""", emptyList())
		val repository = SeerrRepository(client, preferences(signedIn = true), UserRepositoryImpl(), clock = { now })

		repository.serviceDetails(SeerrMediaType.TV, 3)
		now += 4 * 60 * 1000L
		repository.serviceDetails(SeerrMediaType.TV, 3)
		coVerify(exactly = 1) { client.get(any(), "/service/sonarr/3", any(), any()) }

		now += 2 * 60 * 1000L
		repository.serviceDetails(SeerrMediaType.TV, 3)
		coVerify(exactly = 2) { client.get(any(), "/service/sonarr/3", any(), any()) }
	}

	test("server lists drop 4K servers") {
		val client = mockk<SeerrClient>()
		coEvery { client.get(any(), "/service/radarr", any(), any()) } returns SeerrResponse(
			"""[{"id":0,"name":"Main","is4k":false,"isDefault":true},{"id":1,"name":"4K","is4k":true,"isDefault":true}]""",
			emptyList(),
		)
		val repository = SeerrRepository(client, preferences(signedIn = true), UserRepositoryImpl())

		repository.requestServers(SeerrMediaType.MOVIE).map { it.id } shouldBe listOf(0)
	}

	test("calls send the stored cookie to the stored server") {
		val client = mockk<SeerrClient>()
		coEvery { client.get(any(), any(), any(), any()) } returns SeerrResponse(pageBody, emptyList())
		val repository = SeerrRepository(client, preferences(signedIn = true), UserRepositoryImpl())

		repository.discover(SeerrDiscoverRow.POPULAR_TV, 2)
		coVerify { client.get("http://seerr.test:5055", "/discover/tv", "connect.sid=abc", mapOf("page" to "2")) }
	}
})
