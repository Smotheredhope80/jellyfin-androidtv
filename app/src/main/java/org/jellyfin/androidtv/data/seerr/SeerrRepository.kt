package org.jellyfin.androidtv.data.seerr

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.jellyfin.androidtv.auth.repository.UserRepository
import org.jellyfin.androidtv.preference.SeerrPreferences
import timber.log.Timber
import java.time.LocalDate
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException

data class SeerrSession(
	val serverUrl: String,
	val cookie: String,
	val jellyfinUserId: String,
	val seerrUserId: Int,
	val seerrUserName: String,
)

/** Thrown when Seerr no longer accepts the stored session; the session has already been cleared. */
class SeerrSignedOutException : Exception("Seerr session expired")

/** Thrown when request options were chosen but the Seerr user may no longer choose them. */
class SeerrAdvancedNotAllowedException : Exception("Advanced requests are not allowed for this Seerr user")

private const val PERMISSIONS_MAX_AGE_MS = 10 * 60 * 1000L
private const val SERVICE_DETAILS_MAX_AGE_MS = 5 * 60 * 1000L

enum class SeerrDiscoverRow(val path: String, val fallbackType: SeerrMediaType?) {
	TRENDING("/discover/trending", null),
	POPULAR_MOVIES("/discover/movies", SeerrMediaType.MOVIE),
	POPULAR_TV("/discover/tv", SeerrMediaType.TV),
	UPCOMING_MOVIES("/discover/movies", SeerrMediaType.MOVIE),
}

data class SeerrCardPage(
	val cards: List<SeerrCard>,
	val page: Int,
	val totalPages: Int,
)

class SeerrRepository(
	private val client: SeerrClient,
	private val preferences: SeerrPreferences,
	userRepository: UserRepository,
	/** Country used to pick certifications; Seerr's own region setting is not public. */
	private val region: () -> String = { Locale.getDefault().country },
	private val clock: () -> Long = System::currentTimeMillis,
) {
	private val _session = MutableStateFlow(loadSession())
	val session: StateFlow<SeerrSession?> = _session.asStateFlow()

	// Memory only: permissions are re-read from Seerr and never stored
	private data class Timed<T>(val value: T, val at: Long)

	private var permissions: Timed<Long>? = null
	private val serviceDetailsCache = mutableMapOf<Pair<SeerrMediaType, Int>, Timed<SeerrServiceDetails>>()

	/** True only when a Seerr session exists for the Jellyfin user that is signed in now. */
	val available: Flow<Boolean> = combine(session, userRepository.currentUser) { session, user ->
		session != null && user != null && session.jellyfinUserId == user.id.toString()
	}

	private fun loadSession(): SeerrSession? {
		val serverUrl = preferences[SeerrPreferences.serverUrl]
		val cookie = preferences[SeerrPreferences.sessionCookie]
		val jellyfinUserId = preferences[SeerrPreferences.jellyfinUserId]
		if (serverUrl.isBlank() || cookie.isBlank() || jellyfinUserId.isBlank()) return null
		return SeerrSession(
			serverUrl = serverUrl,
			cookie = cookie,
			jellyfinUserId = jellyfinUserId,
			seerrUserId = preferences[SeerrPreferences.seerrUserId],
			seerrUserName = preferences[SeerrPreferences.seerrUserName],
		)
	}

	/** Last server address used, kept after sign out so it can be prefilled. */
	val lastServerUrl get() = preferences[SeerrPreferences.serverUrl]

	/**
	 * Signs in with the Jellyfin username and password. The password is only sent to Seerr; just the
	 * returned session cookie is stored.
	 */
	suspend fun signIn(serverUrl: String, username: String, password: String, jellyfinUserId: String): SeerrUser {
		val url = normalizeSeerrUrl(serverUrl) ?: throw SeerrHttpException(0, "Invalid Seerr address")
		val body = buildJsonObject {
			put("username", username)
			put("password", password)
		}.toString()

		val response = client.post(url, "/auth/jellyfin", cookie = null, body = body)
		val cookie = seerrSessionCookie(response.setCookies)
			?: throw SeerrHttpException(0, "Seerr did not return a session")
		val user = SeerrClient.json.decodeFromString<SeerrUser>(response.body)

		preferences[SeerrPreferences.serverUrl] = url
		preferences[SeerrPreferences.sessionCookie] = cookie
		preferences[SeerrPreferences.jellyfinUserId] = jellyfinUserId
		preferences[SeerrPreferences.seerrUserId] = user.id
		preferences[SeerrPreferences.seerrUserName] = user.label
		clearCaches()
		permissions = Timed(user.permissions, clock())
		_session.value = loadSession()
		return user
	}

	suspend fun signOut() {
		val current = _session.value
		clearSession()
		if (current != null) {
			try {
				client.post(current.serverUrl, "/auth/logout", current.cookie, body = "{}")
			} catch (err: CancellationException) {
				throw err
			} catch (err: Exception) {
				// The local session is already gone; a failed server logout only leaves the cookie to expire
				Timber.w(err, "Seerr logout failed")
			}
		}
	}

	private fun clearSession() {
		preferences.delete(SeerrPreferences.sessionCookie)
		preferences.delete(SeerrPreferences.jellyfinUserId)
		preferences.delete(SeerrPreferences.seerrUserId)
		preferences.delete(SeerrPreferences.seerrUserName)
		clearCaches()
		_session.value = null
	}

	private fun clearCaches() {
		permissions = null
		synchronized(serviceDetailsCache) { serviceDetailsCache.clear() }
	}

	/**
	 * Runs an authenticated call. Seerr answers both an expired session and a missing permission with
	 * 401/403, so a rejection is checked against `/auth/me` before the session is cleared.
	 */
	private suspend fun <T> authenticated(block: suspend (SeerrSession) -> T): T {
		val session = _session.value ?: throw SeerrSignedOutException()
		try {
			return block(session)
		} catch (err: SeerrHttpException) {
			if (err.code != 401 && err.code != 403) throw err
			val stillValid = try {
				client.get(session.serverUrl, "/auth/me", session.cookie)
				true
			} catch (meErr: SeerrHttpException) {
				meErr.code != 401 && meErr.code != 403
			}
			if (stillValid) throw err
			clearSession()
			throw SeerrSignedOutException()
		}
	}

	suspend fun discover(row: SeerrDiscoverRow, page: Int): SeerrCardPage = authenticated { session ->
		val query = buildMap {
			put("page", page.toString())
			if (row == SeerrDiscoverRow.UPCOMING_MOVIES) {
				put("primaryReleaseDateGte", LocalDate.now().toString())
				put("sortBy", "popularity.desc")
			}
		}
		val response = client.get(session.serverUrl, row.path, session.cookie, query)
		decodePage(response.body, row.fallbackType)
	}

	suspend fun search(query: String, page: Int = 1): SeerrCardPage = authenticated { session ->
		val response = client.get(session.serverUrl, "/search", session.cookie, mapOf("query" to query, "page" to page.toString()))
		decodePage(response.body, fallbackType = null)
	}

	private fun decodePage(body: String, fallbackType: SeerrMediaType?): SeerrCardPage {
		val page = SeerrClient.json.decodeFromString<SeerrPage<SeerrSearchResult>>(body)
		return SeerrCardPage(
			cards = page.results.mapNotNull { it.toCard(fallbackType) },
			page = page.page,
			totalPages = page.totalPages,
		)
	}

	suspend fun title(type: SeerrMediaType, tmdbId: Int): SeerrTitle = authenticated { session ->
		val body = client.get(session.serverUrl, "/${type.apiName}/$tmdbId", session.cookie).body
		when (type) {
			SeerrMediaType.MOVIE -> SeerrClient.json.decodeFromString<SeerrMovieDetails>(body).toTitle(region())
			SeerrMediaType.TV -> SeerrClient.json.decodeFromString<SeerrTvDetails>(body).toTitle(region())
		}
	}

	/** Rotten Tomatoes (and, for films, IMDb) scores. Seerr answers 404 when it has none. */
	suspend fun ratings(type: SeerrMediaType, tmdbId: Int): SeerrRatings = authenticated { session ->
		val path = when (type) {
			SeerrMediaType.MOVIE -> "/movie/$tmdbId/ratingscombined"
			SeerrMediaType.TV -> "/tv/$tmdbId/ratings"
		}
		val body = try {
			client.get(session.serverUrl, path, session.cookie).body
		} catch (err: SeerrHttpException) {
			if (err.code == 404) return@authenticated SeerrRatings()
			throw err
		}
		when (type) {
			SeerrMediaType.MOVIE -> SeerrClient.json.decodeFromString<SeerrCombinedRatings>(body).toRatings()
			SeerrMediaType.TV -> SeerrClient.json.decodeFromString<SeerrRtRating>(body).toRatings()
		}
	}

	/** Whether the signed-in Seerr user may choose server, profile, folder and tags. */
	suspend fun canRequestAdvanced(): Boolean {
		val cached = permissions
		val value = if (cached != null && clock() - cached.at < PERMISSIONS_MAX_AGE_MS) {
			cached.value
		} else {
			authenticated { session ->
				val user = SeerrClient.json.decodeFromString<SeerrUser>(client.get(session.serverUrl, "/auth/me", session.cookie).body)
				permissions = Timed(user.permissions, clock())
				user.permissions
			}
		}
		return hasAdvancedRequestPermission(value)
	}

	/** Non-4K Radarr (films) or Sonarr (shows) servers, default first. */
	suspend fun requestServers(type: SeerrMediaType): List<SeerrServiceServer> = authenticated { session ->
		val body = client.get(session.serverUrl, "/service/${type.arrName}", session.cookie).body
		filterRequestServers(SeerrClient.json.decodeFromString<List<SeerrServiceServer>>(body))
	}

	/** Profiles, root folders, tags and defaults for one server, cached for a few minutes. */
	suspend fun serviceDetails(type: SeerrMediaType, serverId: Int): SeerrServiceDetails {
		val key = type to serverId
		synchronized(serviceDetailsCache) { serviceDetailsCache[key] }
			?.takeIf { clock() - it.at < SERVICE_DETAILS_MAX_AGE_MS }
			?.let { return it.value }

		return authenticated { session ->
			val body = client.get(session.serverUrl, "/service/${type.arrName}/$serverId", session.cookie).body
			SeerrClient.json.decodeFromString<SeerrServiceDetails>(body).also { details ->
				synchronized(serviceDetailsCache) { serviceDetailsCache[key] = Timed(details, clock()) }
			}
		}
	}

	suspend fun request(
		type: SeerrMediaType,
		tmdbId: Int,
		seasons: Collection<Int>,
		advanced: SeerrAdvancedRequest? = null,
	): SeerrRequestOutcome {
		// The server accepts these fields from anyone, so re-check before sending them
		if (advanced != null && !canRequestAdvanced()) throw SeerrAdvancedNotAllowedException()

		return authenticated { session ->
			val body = seerrRequestBody(type, tmdbId, seasons, advanced)
			val response = client.post(session.serverUrl, "/request", session.cookie, body)
			seerrRequestOutcome(SeerrClient.json.decodeFromString<SeerrRequest>(response.body).status)
		}
	}

	/** The signed-in user's latest requests as cards. Request entries carry no title, so details are fetched per item. */
	suspend fun myRequests(limit: Int = 10): List<SeerrCard> = authenticated { session ->
		val response = client.get(
			session.serverUrl, "/request", session.cookie,
			mapOf("take" to limit.toString(), "requestedBy" to session.seerrUserId.toString(), "sort" to "added"),
		)
		val requests = SeerrClient.json.decodeFromString<RequestPage>(response.body).results
		coroutineScope {
			requests
				.mapNotNull { request ->
					val tmdbId = request.media?.tmdbId ?: return@mapNotNull null
					val type = SeerrMediaType.fromApi(request.type ?: request.media.mediaType) ?: return@mapNotNull null
					type to tmdbId
				}
				.distinct()
				.map { (type, tmdbId) ->
					async {
						// One missing title should not hide the rest of the row
						val title = try {
							title(type, tmdbId)
						} catch (err: CancellationException) {
							throw err
						} catch (err: SeerrSignedOutException) {
							throw err
						} catch (err: Exception) {
							Timber.w(err, "Seerr request title $tmdbId failed")
							null
						}
						title?.let { SeerrCard(it.type, it.tmdbId, it.title, it.year, it.posterPath, it.availability) }
					}
				}
				.awaitAll()
				.filterNotNull()
		}
	}

	@Serializable
	private data class RequestPage(val results: List<SeerrRequest> = emptyList())
}
