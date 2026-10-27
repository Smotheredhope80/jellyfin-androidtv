package org.jellyfin.androidtv.data.seerr

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
 * Wire models for the Seerr REST API (`/api/v1`), limited to the fields this client uses.
 * Field names follow Seerr's public OpenAPI description. `jellyfinMediaId`, `MediaInfo.seasons`,
 * `MediaRequest.type` and `MediaRequest.seasons` are returned by Jellyfin-backed Seerr servers but are
 * not described in that file, so they are optional and the UI degrades when they are missing.
 */

@Serializable
data class SeerrUser(
	val id: Int,
	val displayName: String? = null,
	val username: String? = null,
	val jellyfinUsername: String? = null,
	val email: String? = null,
	val permissions: Long = 0,
) {
	val label get() = listOfNotNull(displayName, jellyfinUsername, username, email).firstOrNull { it.isNotBlank() } ?: "#$id"
}

@Serializable
data class SeerrPage<T>(
	val page: Int = 1,
	val totalPages: Int = 1,
	val results: List<T> = emptyList(),
)

@Serializable
data class SeerrSearchResult(
	val id: Int,
	val mediaType: String? = null,
	val title: String? = null,
	val name: String? = null,
	val posterPath: String? = null,
	val backdropPath: String? = null,
	val releaseDate: String? = null,
	val firstAirDate: String? = null,
	val overview: String? = null,
	val mediaInfo: SeerrMediaInfo? = null,
)

@Serializable
data class SeerrMediaInfo(
	val tmdbId: Int? = null,
	val mediaType: String? = null,
	val status: Int = SeerrMediaStatus.UNKNOWN,
	val jellyfinMediaId: String? = null,
	val seasons: List<SeerrSeasonStatus> = emptyList(),
	val requests: List<SeerrRequest> = emptyList(),
)

@Serializable
data class SeerrSeasonStatus(
	val seasonNumber: Int,
	val status: Int = SeerrMediaStatus.UNKNOWN,
)

@Serializable
data class SeerrRequest(
	val id: Int,
	val status: Int,
	val type: String? = null,
	val media: SeerrMediaInfo? = null,
	val seasons: List<SeerrSeasonStatus> = emptyList(),
)

@Serializable
data class SeerrMovieDetails(
	val id: Int,
	val title: String? = null,
	val releaseDate: String? = null,
	val runtime: Int? = null,
	val overview: String? = null,
	val posterPath: String? = null,
	val backdropPath: String? = null,
	val mediaInfo: SeerrMediaInfo? = null,
	val tagline: String? = null,
	val status: String? = null,
	val genres: List<SeerrNamed> = emptyList(),
	val relatedVideos: List<SeerrVideo> = emptyList(),
	val budget: Long? = null,
	val revenue: Long? = null,
	val voteAverage: Double? = null,
	val voteCount: Int? = null,
	val productionCompanies: List<SeerrNamed> = emptyList(),
	val credits: SeerrCredits? = null,
	val releases: SeerrReleases? = null,
)

@Serializable
data class SeerrTvDetails(
	val id: Int,
	val name: String? = null,
	val firstAirDate: String? = null,
	val overview: String? = null,
	val posterPath: String? = null,
	val backdropPath: String? = null,
	val seasons: List<SeerrSeason> = emptyList(),
	val mediaInfo: SeerrMediaInfo? = null,
	val tagline: String? = null,
	val status: String? = null,
	val genres: List<SeerrNamed> = emptyList(),
	val relatedVideos: List<SeerrVideo> = emptyList(),
	val voteAverage: Double? = null,
	val voteCount: Int? = null,
	val lastAirDate: String? = null,
	val nextEpisodeToAir: SeerrEpisode? = null,
	val episodeRunTime: List<Int> = emptyList(),
	val networks: List<SeerrNamed> = emptyList(),
	val createdBy: List<SeerrNamed> = emptyList(),
	val credits: SeerrCredits? = null,
	val contentRatings: SeerrContentRatings? = null,
	val keywords: List<SeerrNamed> = emptyList(),
)

/** A genre, company, network, keyword or creator: only the id and name are used. */
@Serializable
data class SeerrNamed(
	val id: Int? = null,
	val name: String? = null,
)

@Serializable
data class SeerrVideo(
	val site: String? = null,
	val key: String? = null,
	val name: String? = null,
	val type: String? = null,
)

@Serializable
data class SeerrCredits(
	val cast: List<SeerrCastMember> = emptyList(),
	val crew: List<SeerrCrewMember> = emptyList(),
)

@Serializable
data class SeerrCastMember(
	val name: String? = null,
	val order: Int? = null,
)

@Serializable
data class SeerrCrewMember(
	val name: String? = null,
	val job: String? = null,
)

@Serializable
data class SeerrEpisode(
	val airDate: String? = null,
)

/** TMDB release dates, passed through unchanged by Seerr (snake_case keys). */
@Serializable
data class SeerrReleases(
	val results: List<SeerrReleaseCountry> = emptyList(),
)

@Serializable
data class SeerrReleaseCountry(
	@SerialName("iso_3166_1") val country: String? = null,
	@SerialName("release_dates") val releaseDates: List<SeerrReleaseDate> = emptyList(),
)

@Serializable
data class SeerrReleaseDate(
	val certification: String? = null,
	val type: Int? = null,
)

@Serializable
data class SeerrContentRatings(
	val results: List<SeerrContentRating> = emptyList(),
)

@Serializable
data class SeerrContentRating(
	@SerialName("iso_3166_1") val country: String? = null,
	val rating: String? = null,
)

/** `GET /movie/{id}/ratingscombined`; either part is absent when unknown. */
@Serializable
data class SeerrCombinedRatings(
	val rt: SeerrRtRating? = null,
	val imdb: SeerrImdbRating? = null,
)

/** Rotten Tomatoes scores; `GET /tv/{id}/ratings` returns this shape directly. */
@Serializable
data class SeerrRtRating(
	// Doubles so a fractional score from the scraper still parses; shown rounded
	val criticsScore: Double? = null,
	val criticsRating: String? = null,
	val audienceScore: Double? = null,
)

@Serializable
data class SeerrImdbRating(
	val criticsScore: Double? = null,
)

/**
 * A Radarr or Sonarr server from `/service/{radarr,sonarr}` and `/service/{arr}/{id}`. The list endpoint
 * leaves Sonarr's `activeTags` empty, so defaults are only taken from the details response.
 */
@Serializable
data class SeerrServiceServer(
	val id: Int,
	val name: String? = null,
	val is4k: Boolean = false,
	val isDefault: Boolean = false,
	val activeProfileId: Int? = null,
	val activeDirectory: String? = null,
	val activeLanguageProfileId: Int? = null,
	val activeAnimeProfileId: Int? = null,
	val activeAnimeDirectory: String? = null,
	val activeAnimeLanguageProfileId: Int? = null,
	val activeTags: List<Int>? = null,
	val activeAnimeTags: List<Int>? = null,
)

/** `GET /service/{arr}/{id}`. `rootFolders`, `tags` and `languageProfiles` are returned but not in the OpenAPI file. */
@Serializable
data class SeerrServiceDetails(
	val server: SeerrServiceServer,
	val profiles: List<SeerrServiceProfile> = emptyList(),
	val rootFolders: List<SeerrRootFolder> = emptyList(),
	val languageProfiles: List<SeerrServiceProfile>? = null,
	val tags: List<SeerrServiceTag> = emptyList(),
)

@Serializable
data class SeerrServiceProfile(
	val id: Int,
	val name: String? = null,
)

@Serializable
data class SeerrRootFolder(
	val path: String,
	val freeSpace: Long? = null,
)

@Serializable
data class SeerrServiceTag(
	val id: Int,
	val label: String? = null,
)

@Serializable
data class SeerrSeason(
	val seasonNumber: Int,
	val episodeCount: Int = 0,
	val name: String? = null,
)

@Serializable
data class SeerrError(
	val message: String? = null,
	val error: String? = null,
)

/** Numeric media status values from Seerr's `MediaInfo.status`. */
object SeerrMediaStatus {
	const val UNKNOWN = 1
	const val PENDING = 2
	const val PROCESSING = 3
	const val PARTIALLY_AVAILABLE = 4
	const val AVAILABLE = 5
	const val DELETED = 6
}

/** Seerr user permission bits (`server/lib/permissions.ts`) used by this client. */
object SeerrPermission {
	const val ADMIN = 2L
	const val MANAGE_REQUESTS = 16L
	const val REQUEST_ADVANCED = 8192L
}

/** Numeric request status values from Seerr's `MediaRequest.status`. */
object SeerrRequestStatus {
	const val PENDING_APPROVAL = 1
	const val APPROVED = 2
	const val DECLINED = 3
}
