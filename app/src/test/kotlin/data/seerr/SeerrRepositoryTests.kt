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

	test("calls send the stored cookie to the stored server") {
		val client = mockk<SeerrClient>()
		coEvery { client.get(any(), any(), any(), any()) } returns SeerrResponse(pageBody, emptyList())
		val repository = SeerrRepository(client, preferences(signedIn = true), UserRepositoryImpl())

		repository.discover(SeerrDiscoverRow.POPULAR_TV, 2)
		coVerify { client.get("http://seerr.test:5055", "/discover/tv", "connect.sid=abc", mapOf("page" to "2")) }
	}
})
