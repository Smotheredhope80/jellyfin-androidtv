package org.jellyfin.androidtv.ui.seerr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jellyfin.androidtv.data.seerr.SeerrAdvancedNotAllowedException
import org.jellyfin.androidtv.data.seerr.SeerrAdvancedRequest
import org.jellyfin.androidtv.data.seerr.SeerrMediaType
import org.jellyfin.androidtv.data.seerr.SeerrRatings
import org.jellyfin.androidtv.data.seerr.SeerrRepository
import org.jellyfin.androidtv.data.seerr.SeerrRequestOptions
import org.jellyfin.androidtv.data.seerr.SeerrRequestOutcome
import org.jellyfin.androidtv.data.seerr.SeerrServiceDetails
import org.jellyfin.androidtv.data.seerr.SeerrServiceServer
import org.jellyfin.androidtv.data.seerr.SeerrSignedOutException
import org.jellyfin.androidtv.data.seerr.SeerrTitle
import org.jellyfin.androidtv.data.seerr.defaultRequestOptions
import org.jellyfin.androidtv.data.seerr.defaultServerId
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

data class SeerrDetailsState(
	val title: SeerrTitle? = null,
	val ratings: SeerrRatings? = null,
	val selectedSeasons: Set<Int> = emptySet(),
	val loading: Boolean = true,
	val requesting: Boolean = false,
	val error: String? = null,
	val signedOut: Boolean = false,
	val canRequestAdvanced: Boolean = false,
	/** The request options panel; null while closed. */
	val options: SeerrOptionsState? = null,
)

enum class SeerrOptionPicker { SERVER, PROFILE, FOLDER, LANGUAGE, TAGS }

data class SeerrOptionsState(
	val loading: Boolean = true,
	val error: String? = null,
	val servers: List<SeerrServiceServer> = emptyList(),
	val defaultServerId: Int? = null,
	val details: SeerrServiceDetails? = null,
	val defaults: SeerrRequestOptions? = null,
	val selected: SeerrRequestOptions? = null,
	/** The chosen server could not be loaded, so the first server is shown again. */
	val serverFellBack: Boolean = false,
	val picker: SeerrOptionPicker? = null,
) {
	val ready get() = !loading && details != null && defaults != null && selected != null
}

sealed interface SeerrRequestEvent {
	data class Done(val outcome: SeerrRequestOutcome) : SeerrRequestEvent
	data class Failed(val message: String) : SeerrRequestEvent
	data object NotAllowed : SeerrRequestEvent
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

	private var optionsJob: Job? = null

	init {
		reload()
		loadRatings()
		loadPermission()
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

	/** Ratings are optional extras: a failure only leaves them out. */
	private fun loadRatings() {
		viewModelScope.launch {
			try {
				val ratings = repository.ratings(type, tmdbId)
				_state.update { it.copy(ratings = ratings) }
			} catch (err: CancellationException) {
				throw err
			} catch (err: SeerrSignedOutException) {
				_state.update { it.copy(signedOut = true) }
			} catch (err: Exception) {
				Timber.w(err, "Failed to load Seerr ratings")
			}
		}
	}

	/** Without a confirmed permission the plain request is used, as in phase 1. */
	private fun loadPermission() {
		viewModelScope.launch {
			try {
				val allowed = repository.canRequestAdvanced()
				_state.update { it.copy(canRequestAdvanced = allowed) }
			} catch (err: CancellationException) {
				throw err
			} catch (err: SeerrSignedOutException) {
				_state.update { it.copy(signedOut = true) }
			} catch (err: Exception) {
				Timber.w(err, "Failed to read Seerr permissions")
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

	/** The details screen's Request button: opens the options panel for advanced users. */
	fun requestPressed() {
		if (_state.value.canRequestAdvanced) openOptions() else request(advanced = null)
	}

	private fun openOptions() {
		if (_state.value.options != null) return
		_state.update { it.copy(options = SeerrOptionsState()) }
		optionsJob?.cancel()
		optionsJob = viewModelScope.launch {
			try {
				val servers = repository.requestServers(type)
				val first = servers.firstOrNull()
				if (first == null) {
					updateOptions { it.copy(loading = false, error = null, servers = emptyList()) }
					return@launch
				}
				updateOptions { it.copy(servers = servers, defaultServerId = defaultServerId(servers)) }
				loadServer(first.id)
			} catch (err: CancellationException) {
				throw err
			} catch (err: SeerrSignedOutException) {
				_state.update { it.copy(signedOut = true, options = null) }
			} catch (err: Exception) {
				Timber.w(err, "Failed to load Seerr servers")
				updateOptions { it.copy(loading = false, error = err.message ?: err.javaClass.simpleName) }
			}
		}
	}

	private suspend fun loadServer(serverId: Int) {
		val details = repository.serviceDetails(type, serverId)
		val defaults = defaultRequestOptions(details, anime = type == SeerrMediaType.TV && _state.value.title?.anime == true)
		updateOptions {
			it.copy(loading = false, error = null, details = details, defaults = defaults, selected = defaults)
		}
	}

	fun selectServer(serverId: Int) {
		val options = _state.value.options ?: return
		if (options.selected?.serverId == serverId) return closePicker()
		val previous = options.details?.server?.id
		updateOptions { it.copy(loading = true, picker = null, serverFellBack = false) }
		optionsJob?.cancel()
		optionsJob = viewModelScope.launch {
			try {
				loadServer(serverId)
			} catch (err: CancellationException) {
				throw err
			} catch (err: SeerrSignedOutException) {
				_state.update { it.copy(signedOut = true, options = null) }
			} catch (err: Exception) {
				Timber.w(err, "Failed to load Seerr server $serverId")
				// Go back to the server that loaded before, saying so
				val fallback = previous ?: options.servers.firstOrNull()?.id
				try {
					if (fallback == null || fallback == serverId) throw err
					loadServer(fallback)
					updateOptions { it.copy(serverFellBack = true) }
				} catch (fallbackErr: CancellationException) {
					throw fallbackErr
				} catch (fallbackErr: SeerrSignedOutException) {
					_state.update { it.copy(signedOut = true, options = null) }
				} catch (fallbackErr: Exception) {
					// Drop the old server's values so the panel shows the error and the defaults fallback
					updateOptions {
						it.copy(loading = false, error = err.message ?: err.javaClass.simpleName, details = null, defaults = null, selected = null)
					}
				}
			}
		}
	}

	fun openPicker(picker: SeerrOptionPicker) = updateOptions { if (it.ready) it.copy(picker = picker) else it }

	fun closePicker() = updateOptions { it.copy(picker = null) }

	fun selectProfile(id: Int) = updateSelection(closePicker = true) { it.copy(profileId = id) }

	fun selectFolder(path: String) = updateSelection(closePicker = true) { it.copy(rootFolder = path) }

	fun selectLanguageProfile(id: Int) = updateSelection(closePicker = true) { it.copy(languageProfileId = id) }

	fun toggleTag(id: Int) = updateSelection(closePicker = false) {
		it.copy(tags = if (id in it.tags) it.tags - id else it.tags + id)
	}

	fun closeOptions() {
		optionsJob?.cancel()
		_state.update { it.copy(options = null) }
	}

	fun confirmOptions() {
		val options = _state.value.options ?: return
		if (!options.ready) return
		request(SeerrAdvancedRequest(options.selected!!, options.defaults!!, options.defaultServerId))
	}

	/** Sends the phase 1 request, used when the options can't be loaded. */
	fun requestWithDefaults() = request(advanced = null)

	private fun request(advanced: SeerrAdvancedRequest?) {
		val current = _state.value
		val title = current.title ?: return
		if (current.requesting || !title.canRequest) return
		val seasons = if (title.type == SeerrMediaType.TV) current.selectedSeasons else emptySet()
		if (title.type == SeerrMediaType.TV && seasons.isEmpty()) return

		_state.update { it.copy(requesting = true) }
		viewModelScope.launch {
			try {
				val outcome = repository.request(title.type, title.tmdbId, seasons, advanced)
				_events.value = SeerrRequestEvent.Done(outcome)
				closeOptions()
				reload()
			} catch (err: CancellationException) {
				throw err
			} catch (err: SeerrSignedOutException) {
				_state.update { it.copy(signedOut = true, options = null) }
			} catch (err: SeerrAdvancedNotAllowedException) {
				_state.update { it.copy(canRequestAdvanced = false, options = null) }
				_events.value = SeerrRequestEvent.NotAllowed
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

	private fun updateOptions(block: (SeerrOptionsState) -> SeerrOptionsState) {
		_state.update { state -> state.options?.let { state.copy(options = block(it)) } ?: state }
	}

	private fun updateSelection(closePicker: Boolean, block: (SeerrRequestOptions) -> SeerrRequestOptions) = updateOptions { options ->
		val selected = options.selected ?: return@updateOptions options
		options.copy(selected = block(selected), picker = if (closePicker) null else options.picker)
	}
}
