package org.jellyfin.androidtv.data.seerr

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.UUID

enum class SeerrMediaType(val apiName: String) {
	MOVIE("movie"),
	TV("tv");

	companion object {
		fun fromApi(value: String?) = entries.firstOrNull { it.apiName == value }
	}
}

/** What the user can do with a title, collapsed from Seerr's media status. */
enum class SeerrAvailability {
	NONE,
	PENDING,
	PROCESSING,
	PARTIAL,
	AVAILABLE,
}

fun seerrAvailability(status: Int?) = when (status) {
	SeerrMediaStatus.PENDING -> SeerrAvailability.PENDING
	SeerrMediaStatus.PROCESSING -> SeerrAvailability.PROCESSING
	SeerrMediaStatus.PARTIALLY_AVAILABLE -> SeerrAvailability.PARTIAL
	SeerrMediaStatus.AVAILABLE -> SeerrAvailability.AVAILABLE
	// Unknown and deleted media can be requested again
	else -> SeerrAvailability.NONE
}

/** A poster card on the Discover screen. */
data class SeerrCard(
	val type: SeerrMediaType,
	val tmdbId: Int,
	val title: String,
	val year: String?,
	val posterPath: String?,
	val availability: SeerrAvailability,
)

data class SeerrSeasonChoice(
	val number: Int,
	val name: String,
	val episodeCount: Int,
	val availability: SeerrAvailability,
) {
	val requestable get() = availability == SeerrAvailability.NONE
}

/** A movie or show on the details screen. */
data class SeerrTitle(
	val type: SeerrMediaType,
	val tmdbId: Int,
	val title: String,
	val year: String?,
	val overview: String?,
	val posterPath: String?,
	val backdropPath: String?,
	val runtimeMinutes: Int?,
	val availability: SeerrAvailability,
	val jellyfinItemId: UUID?,
	val seasons: List<SeerrSeasonChoice>,
) {
	val canRequest
		get() = when (type) {
			SeerrMediaType.MOVIE -> availability == SeerrAvailability.NONE
			SeerrMediaType.TV -> seasons.any { it.requestable }
		}
}

enum class SeerrRequestOutcome {
	APPROVED,
	WAITING_FOR_APPROVAL,
	DECLINED,
}

fun seerrRequestOutcome(status: Int) = when (status) {
	SeerrRequestStatus.APPROVED -> SeerrRequestOutcome.APPROVED
	SeerrRequestStatus.DECLINED -> SeerrRequestOutcome.DECLINED
	else -> SeerrRequestOutcome.WAITING_FOR_APPROVAL
}

private fun String?.year() = this?.takeIf { it.length >= 4 }?.take(4)

/** Maps a search or discover result to a card, or null for people and unknown types. */
fun SeerrSearchResult.toCard(fallbackType: SeerrMediaType? = null): SeerrCard? {
	val type = SeerrMediaType.fromApi(mediaType) ?: fallbackType.takeIf { mediaType == null } ?: return null
	val title = (if (type == SeerrMediaType.MOVIE) title else name)?.takeIf { it.isNotBlank() } ?: return null
	return SeerrCard(
		type = type,
		tmdbId = id,
		title = title,
		year = (if (type == SeerrMediaType.MOVIE) releaseDate else firstAirDate).year(),
		posterPath = posterPath,
		availability = seerrAvailability(mediaInfo?.status),
	)
}

fun SeerrMovieDetails.toTitle() = SeerrTitle(
	type = SeerrMediaType.MOVIE,
	tmdbId = id,
	title = title.orEmpty(),
	year = releaseDate.year(),
	overview = overview,
	posterPath = posterPath,
	backdropPath = backdropPath,
	runtimeMinutes = runtime?.takeIf { it > 0 },
	availability = seerrAvailability(mediaInfo?.status),
	jellyfinItemId = parseJellyfinId(mediaInfo?.jellyfinMediaId),
	seasons = emptyList(),
)

fun SeerrTvDetails.toTitle() = SeerrTitle(
	type = SeerrMediaType.TV,
	tmdbId = id,
	title = name.orEmpty(),
	year = firstAirDate.year(),
	overview = overview,
	posterPath = posterPath,
	backdropPath = backdropPath,
	runtimeMinutes = null,
	availability = seerrAvailability(mediaInfo?.status),
	jellyfinItemId = parseJellyfinId(mediaInfo?.jellyfinMediaId),
	seasons = seasonChoices(this),
)

/**
 * Season list for a show, without specials (season 0). A season's own media status wins; otherwise a
 * season named in an open (not declined) request counts as pending.
 */
fun seasonChoices(details: SeerrTvDetails): List<SeerrSeasonChoice> {
	val mediaStatus = details.mediaInfo?.seasons.orEmpty().associate { it.seasonNumber to it.status }
	val requested = details.mediaInfo?.requests.orEmpty()
		.filter { it.status != SeerrRequestStatus.DECLINED }
		.flatMap { request -> request.seasons.map { it.seasonNumber } }
		.toSet()

	return details.seasons
		.filter { it.seasonNumber > 0 }
		.sortedBy { it.seasonNumber }
		.map { season ->
			val ownStatus = seerrAvailability(mediaStatus[season.seasonNumber])
			SeerrSeasonChoice(
				number = season.seasonNumber,
				name = season.name?.takeIf { it.isNotBlank() } ?: "Season ${season.seasonNumber}",
				episodeCount = season.episodeCount,
				availability = when {
					ownStatus != SeerrAvailability.NONE -> ownStatus
					season.seasonNumber in requested -> SeerrAvailability.PENDING
					else -> SeerrAvailability.NONE
				},
			)
		}
}

/**
 * Body for `POST /request`. Only the media and seasons are sent so that Seerr's own defaults, user
 * permissions and quotas decide the server, profile, folder and approval.
 */
fun seerrRequestBody(type: SeerrMediaType, tmdbId: Int, seasons: Collection<Int> = emptyList()): String {
	require(type == SeerrMediaType.MOVIE || seasons.isNotEmpty()) { "A TV request needs at least one season" }
	return buildJsonObject {
		put("mediaType", type.apiName)
		put("mediaId", tmdbId)
		if (type == SeerrMediaType.TV) put("seasons", JsonArray(seasons.sorted().map(::JsonPrimitive)))
	}.toString()
}

/** Normalises a user-entered server address to `scheme://host[:port][/path]` without a trailing slash or API suffix. */
fun normalizeSeerrUrl(input: String): String? {
	val trimmed = input.trim()
	val scheme = when {
		trimmed.startsWith("https://", ignoreCase = true) -> "https"
		trimmed.startsWith("http://", ignoreCase = true) -> "http"
		else -> null
	}
	var rest = (if (scheme != null) trimmed.substringAfter("://") else trimmed).trimEnd('/')
	if (rest.endsWith("/api/v1", ignoreCase = true)) rest = rest.dropLast("/api/v1".length).trimEnd('/')
	val host = rest.substringBefore('/').substringBefore(':')
	if (host.isBlank() || rest.any { it.isWhitespace() }) return null
	return "${scheme ?: "http"}://$rest"
}

/** Parses a Jellyfin item id, which Seerr returns without dashes. */
fun parseJellyfinId(value: String?): UUID? {
	val id = value?.trim() ?: return null
	val dashed = if (id.length == 32 && id.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' }) {
		"${id.substring(0, 8)}-${id.substring(8, 12)}-${id.substring(12, 16)}-${id.substring(16, 20)}-${id.substring(20)}"
	} else id
	return runCatching { UUID.fromString(dashed) }.getOrNull()
}

/** Extracts the `connect.sid` session cookie (name=value only) from `Set-Cookie` headers. */
fun seerrSessionCookie(setCookieHeaders: List<String>): String? = setCookieHeaders
	.map { it.substringBefore(';').trim() }
	.firstOrNull { it.startsWith("$SEERR_SESSION_COOKIE=") && it.length > SEERR_SESSION_COOKIE.length + 1 }

const val SEERR_SESSION_COOKIE = "connect.sid"

fun tmdbImageUrl(path: String?, size: String): String? =
	path?.takeIf { it.startsWith('/') }?.let { "https://image.tmdb.org/t/p/$size$it" }
