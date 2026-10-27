package org.jellyfin.androidtv.data.seerr

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

/** Numeric request status values from Seerr's `MediaRequest.status`. */
object SeerrRequestStatus {
	const val PENDING_APPROVAL = 1
	const val APPROVED = 2
	const val DECLINED = 3
}
