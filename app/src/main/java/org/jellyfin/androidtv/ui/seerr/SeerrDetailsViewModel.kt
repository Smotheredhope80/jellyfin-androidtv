package org.jellyfin.androidtv.ui.seerr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jellyfin.androidtv.data.seerr.SeerrMediaType
import org.jellyfin.androidtv.data.seerr.SeerrRepository
import org.jellyfin.androidtv.data.seerr.SeerrRequestOutcome
import org.jellyfin.androidtv.data.seerr.SeerrSignedOutException
import org.jellyfin.androidtv.data.seerr.SeerrTitle
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

data class SeerrDetailsState(
	val title: SeerrTitle? = null,
	val selectedSeasons: Set<Int> = emptySet(),
	val loading: Boolean = true,
	val requesting: Boolean = false,
	val error: String? = null,
	val signedOut: Boolean = false,
)

sealed interface SeerrRequestEvent {
	data class Done(val outcome: SeerrRequestOutcome) : SeerrRequestEvent
	data class Failed(val message: String) : SeerrRequestEvent
}

class SeerrDetailsViewModel(
	private val repository: SeerrRepository,
	private val type: SeerrMediaType,
	private val tmdbId: Int,
) : ViewModel() {
	private val _state = MutableStateFlow(SeerrDetailsState())
	val state = _state.asStateFlow()

	private val _events = MutableStateFlow<SeerrRequestEvent?>(null)
	val events = _events.asStateFlow()

	init {
		reload()
	}

	private fun reload() {
		viewModelScope.launch {
			try {
				val title = repository.title(type, tmdbId)
				_state.update {
					it.copy(
						title = title,
						// Default to every season that can still be requested
						selectedSeasons = title.seasons.filter { season -> season.requestable }.mapTo(HashSet()) { season -> season.number },
						loading = false,
						error = null,
					)
				}
			} catch (err: CancellationException) {
				throw err
			} catch (err: SeerrSignedOutException) {
				_state.update { it.copy(loading = false, signedOut = true) }
			} catch (err: Exception) {
				Timber.w(err, "Failed to load Seerr title")
				_state.update { it.copy(loading = false, error = err.message ?: err.javaClass.simpleName) }
			}
		}
	}

	fun toggleSeason(number: Int) {
		_state.update { state ->
			val season = state.title?.seasons?.firstOrNull { it.number == number }
			if (season == null || !season.requestable) return@update state
			val selected = if (number in state.selectedSeasons) state.selectedSeasons - number else state.selectedSeasons + number
			state.copy(selectedSeasons = selected)
		}
	}

	fun request() {
		val current = _state.value
		val title = current.title ?: return
		if (current.requesting || !title.canRequest) return
		val seasons = if (title.type == SeerrMediaType.TV) current.selectedSeasons else emptySet()
		if (title.type == SeerrMediaType.TV && seasons.isEmpty()) return

		_state.update { it.copy(requesting = true) }
		viewModelScope.launch {
			try {
				val outcome = repository.request(title.type, title.tmdbId, seasons)
				_events.value = SeerrRequestEvent.Done(outcome)
				reload()
			} catch (err: CancellationException) {
				throw err
			} catch (err: SeerrSignedOutException) {
				_state.update { it.copy(signedOut = true) }
			} catch (err: Exception) {
				Timber.w(err, "Seerr request failed")
				_events.value = SeerrRequestEvent.Failed(err.message ?: err.javaClass.simpleName)
			} finally {
				_state.update { it.copy(requesting = false) }
			}
		}
	}

	fun consumeEvent() {
		_events.value = null
	}
}
