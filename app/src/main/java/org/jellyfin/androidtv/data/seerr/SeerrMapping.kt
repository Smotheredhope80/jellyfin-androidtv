package org.jellyfin.androidtv.data.seerr

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.Locale
import java.util.UUID

enum class SeerrMediaType(val apiName: String, val arrName: String) {
	MOVIE("movie", "radarr"),
	TV("tv", "sonarr");

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
	val trailerKey: String? = null,
	val tmdbScore: Double? = null,
	val info: SeerrInfo = SeerrInfo(),
	val anime: Boolean = false,
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

/**
 * Extra details shown under the overview. Every field is optional and the screen skips empty ones.
 * For a show, [credit] holds the creators and [companies] the networks.
 */
data class SeerrInfo(
	val tagline: String? = null,
	val certification: String? = null,
	val genres: List<String> = emptyList(),
	val status: String? = null,
	val date: String? = null,
	val lastAirDate: String? = null,
	val nextEpisodeDate: String? = null,
	val episodeRuntimeMinutes: Int? = null,
	val revenue: String? = null,
	val budget: String? = null,
	val credit: List<String> = emptyList(),
	val companies: List<String> = emptyList(),
	val cast: List<String> = emptyList(),
)

/** Rotten Tomatoes and IMDb scores; TMDB's score is on [SeerrTitle]. */
data class SeerrRatings(
	val rtCritics: Int? = null,
	val rtCriticsRating: String? = null,
	val rtAudience: Int? = null,
	val imdb: Double? = null,
) {
	val isEmpty get() = rtCritics == null && rtAudience == null && imdb == null
}

fun SeerrCombinedRatings.toRatings() = SeerrRatings(
	rtCritics = rt?.criticsScore?.roundScore(),
	rtCriticsRating = rt?.criticsRating?.takeIf { it.isNotBlank() },
	rtAudience = rt?.audienceScore?.roundScore(),
	imdb = imdb?.criticsScore?.takeIf { it > 0 },
)

fun SeerrRtRating.toRatings() = SeerrCombinedRatings(rt = this).toRatings()

private fun Double.roundScore() = takeIf { it in 0.0..100.0 }?.let { Math.round(it).toInt() }

private const val MAX_GENRES = 3
private const val MAX_COMPANIES = 2
private const val MAX_CAST = 8
private const val FALLBACK_REGION = "US"

/** TMDB's "anime" keyword, which Seerr uses to choose a Sonarr server's anime defaults. */
const val SEERR_ANIME_KEYWORD_ID = 210024

private const val TMDB_THEATRICAL_RELEASE = 3

private val YOUTUBE_KEY_PATTERN = Regex("^[A-Za-z0-9_-]{11}$")

fun SeerrMovieDetails.toTitle(region: String = FALLBACK_REGION) = SeerrTitle(
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
	trailerKey = pickTrailerKey(relatedVideos),
	tmdbScore = tmdbScore(voteAverage, voteCount),
	info = SeerrInfo(
		tagline = tagline.clean(),
		certification = movieCertification(releases, region),
		genres = genres.names(MAX_GENRES),
		status = status.clean(),
		date = releaseDate.clean(),
		revenue = formatUsdCompact(revenue),
		budget = formatUsdCompact(budget),
		credit = credits?.crew.orEmpty().filter { it.job == "Director" }.mapNotNull { it.name.clean() }.distinct().take(MAX_COMPANIES),
		companies = productionCompanies.names(MAX_COMPANIES),
		cast = castNames(credits),
	),
)

fun SeerrTvDetails.toTitle(region: String = FALLBACK_REGION) = SeerrTitle(
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
	trailerKey = pickTrailerKey(relatedVideos),
	tmdbScore = tmdbScore(voteAverage, voteCount),
	info = SeerrInfo(
		tagline = tagline.clean(),
		certification = tvContentRating(contentRatings, region),
		genres = genres.names(MAX_GENRES),
		status = status.clean(),
		date = firstAirDate.clean(),
		lastAirDate = lastAirDate.clean(),
		nextEpisodeDate = nextEpisodeToAir?.airDate.clean(),
		episodeRuntimeMinutes = episodeRunTime.firstOrNull { it > 0 },
		credit = createdBy.names(MAX_COMPANIES),
		companies = networks.names(MAX_COMPANIES),
		cast = castNames(credits),
	),
	anime = isAnime(keywords),
)

private fun String?.clean() = this?.trim()?.takeIf { it.isNotEmpty() }

private fun List<SeerrNamed>.names(limit: Int) = mapNotNull { it.name.clean() }.distinct().take(limit)

private fun castNames(credits: SeerrCredits?) = credits?.cast.orEmpty()
	.sortedBy { it.order ?: Int.MAX_VALUE }
	.mapNotNull { it.name.clean() }
	.distinct()
	.take(MAX_CAST)

private fun tmdbScore(average: Double?, count: Int?) = average?.takeIf { (count ?: 0) > 0 && it > 0 }

/**
 * The YouTube video to offer as the trailer: trailers first, then teasers, then anything else, keeping
 * Seerr's (TMDB's) order within each type. Null when there is no usable YouTube video.
 */
fun pickTrailerKey(videos: List<SeerrVideo>): String? = videos
	.filter { it.site.equals("YouTube", ignoreCase = true) && it.key?.matches(YOUTUBE_KEY_PATTERN) == true }
	.minByOrNull {
		when {
			it.type.equals("Trailer", ignoreCase = true) -> 0
			it.type.equals("Teaser", ignoreCase = true) -> 1
			else -> 2
		}
	}
	?.key

/**
 * A film's certification for [region], falling back to the US. A theatrical release's rating wins, then
 * any non-blank rating for that country.
 */
fun movieCertification(releases: SeerrReleases?, region: String): String? {
	val countries = releases?.results.orEmpty()
	return regionOrder(region).firstNotNullOfOrNull { code ->
		val dates = countries.firstOrNull { it.country.equals(code, ignoreCase = true) }?.releaseDates.orEmpty()
		dates.firstOrNull { it.type == TMDB_THEATRICAL_RELEASE && !it.certification.isNullOrBlank() }?.certification?.trim()
			?: dates.firstOrNull { !it.certification.isNullOrBlank() }?.certification?.trim()
	}
}

/** A show's content rating for [region], falling back to the US. */
fun tvContentRating(ratings: SeerrContentRatings?, region: String): String? {
	val results = ratings?.results.orEmpty()
	return regionOrder(region).firstNotNullOfOrNull { code ->
		results.firstOrNull { it.country.equals(code, ignoreCase = true) }?.rating.clean()
	}
}

private fun regionOrder(region: String) = listOf(region.trim().uppercase(), FALLBACK_REGION).filter { it.length == 2 }.distinct()

/**
 * Formats a TMDB money figure (always USD) as `$950K`, `$200M` or `$1.2B`. Zero and missing values mean
 * unknown and return null. Implemented by hand because compact number formatting needs API 30.
 */
fun formatUsdCompact(value: Long?): String? {
	if (value == null || value <= 0) return null
	val units = listOf(1_000_000_000_000L to "T", 1_000_000_000L to "B", 1_000_000L to "M", 1_000L to "K")
	for ((index, unit) in units.withIndex()) {
		val (size, suffix) = unit
		if (value < size) continue
		val scaled = value.toDouble() / size
		// One decimal below 100 (12.5M), whole numbers above (250M)
		val text = if (scaled < 100) String.format(Locale.US, "%.1f", scaled).removeSuffix(".0") else Math.round(scaled).toString()
		// Rounding 999.6K up would read 1000K; show it in the next unit instead
		if (text == "1000" && index > 0) return "$1${units[index - 1].second}"
		return "$$text$suffix"
	}
	return "$$value"
}

fun isAnime(keywords: List<SeerrNamed>) = keywords.any { it.id == SEERR_ANIME_KEYWORD_ID }

/**
 * Whether the user may choose server, profile, folder and tags. Seerr's request endpoint accepts these
 * fields from anyone and only uses this permission for override rules, so the client must check it.
 */
fun hasAdvancedRequestPermission(permissions: Long) =
	permissions and (SeerrPermission.ADMIN or SeerrPermission.MANAGE_REQUESTS or SeerrPermission.REQUEST_ADVANCED) != 0L

/** The Radarr/Sonarr choices for one request. Null fields mean the server has no value to offer. */
data class SeerrRequestOptions(
	val serverId: Int,
	val profileId: Int?,
	val rootFolder: String?,
	val languageProfileId: Int?,
	val tags: Set<Int>,
)

/**
 * What Seerr would use for this server when nothing is sent, following its request subscriber:
 * a Sonarr server's anime profile, folder, language profile and tags apply to anime shows when set.
 */
fun defaultRequestOptions(details: SeerrServiceDetails, anime: Boolean): SeerrRequestOptions {
	val server = details.server
	return SeerrRequestOptions(
		serverId = server.id,
		profileId = if (anime && server.activeAnimeProfileId != null) server.activeAnimeProfileId else server.activeProfileId,
		rootFolder = if (anime && !server.activeAnimeDirectory.isNullOrBlank()) server.activeAnimeDirectory else server.activeDirectory,
		languageProfileId = if (anime && server.activeAnimeLanguageProfileId != null) server.activeAnimeLanguageProfileId else server.activeLanguageProfileId,
		tags = (if (anime) server.activeAnimeTags else server.activeTags).orEmpty().toSet(),
	)
}

/** Servers offered for a request: not 4K (4K requests are not supported), default first. */
fun filterRequestServers(servers: List<SeerrServiceServer>) = servers
	.filterNot { it.is4k }
	.sortedByDescending { it.isDefault }

/**
 * The server Seerr picks when no `serverId` is sent: the non-4K server marked default. When none is
 * marked, Seerr has no fallback, so this is null and the chosen server is always sent.
 */
fun defaultServerId(servers: List<SeerrServiceServer>) = servers.firstOrNull { it.isDefault && !it.is4k }?.id

/** Formats free disk space as `1.2 TB` or `850 GB`. */
fun formatFreeSpace(bytes: Long?): String? {
	if (bytes == null || bytes < 0) return null
	val gb = bytes / 1_000_000_000.0
	return if (gb >= 1000) String.format(Locale.US, "%.1f TB", gb / 1000) else String.format(Locale.US, "%.0f GB", gb)
}

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

/** Advanced choices for a request with the values Seerr would use anyway, so only changes are sent. */
data class SeerrAdvancedRequest(
	val selected: SeerrRequestOptions,
	val defaults: SeerrRequestOptions,
	val defaultServerId: Int?,
)

/**
 * Body for `POST /request`. Without [advanced], only the media and seasons are sent so that Seerr's own
 * defaults, user permissions and quotas decide the server, profile, folder and approval. With it, only
 * the choices that differ from the chosen server's defaults are added, so Seerr's override rules still
 * apply to anything left alone. `is4k`, `userId` and `ignoreQuota` are never sent.
 */
fun seerrRequestBody(
	type: SeerrMediaType,
	tmdbId: Int,
	seasons: Collection<Int> = emptyList(),
	advanced: SeerrAdvancedRequest? = null,
): String {
	require(type == SeerrMediaType.MOVIE || seasons.isNotEmpty()) { "A TV request needs at least one season" }
	return buildJsonObject {
		put("mediaType", type.apiName)
		put("mediaId", tmdbId)
		if (type == SeerrMediaType.TV) put("seasons", JsonArray(seasons.sorted().map(::JsonPrimitive)))

		if (advanced != null) {
			val selected = advanced.selected
			val defaults = advanced.defaults
			require(selected.serverId == defaults.serverId) { "Defaults must belong to the selected server" }
			if (selected.serverId != advanced.defaultServerId) put("serverId", selected.serverId)
			if (selected.profileId != null && selected.profileId != defaults.profileId) put("profileId", selected.profileId)
			if (selected.rootFolder != null && selected.rootFolder != defaults.rootFolder) put("rootFolder", selected.rootFolder)
			if (selected.languageProfileId != null && selected.languageProfileId != defaults.languageProfileId) put("languageProfileId", selected.languageProfileId)
			if (selected.tags != defaults.tags) put("tags", JsonArray(selected.tags.sorted().map(::JsonPrimitive)))
		}
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
