package org.jellyfin.androidtv.ui.seerr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jellyfin.androidtv.data.seerr.SeerrCard
import org.jellyfin.androidtv.data.seerr.SeerrCardPage
import org.jellyfin.androidtv.data.seerr.SeerrDiscoverRow
import org.jellyfin.androidtv.data.seerr.SeerrRepository
import org.jellyfin.androidtv.data.seerr.SeerrSignedOutException
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

data class SeerrRowState(
	val cards: List<SeerrCard> = emptyList(),
	val page: Int = 0,
	val totalPages: Int = 1,
	val loading: Boolean = false,
	val error: String? = null,
) {
	val canLoadMore get() = !loading && page < totalPages

	/** Appends a page, dropping cards already shown because remote pages can overlap. */
	fun append(result: SeerrCardPage): SeerrRowState {
		val seen = cards.mapTo(HashSet()) { it.type to it.tmdbId }
		return copy(
			cards = cards + result.cards.filter { seen.add(it.type to it.tmdbId) },
			page = result.page,
			totalPages = result.totalPages,
			loading = false,
			error = null,
		)
	}
}

class SeerrDiscoverViewModel(
	private val repository: SeerrRepository,
) : ViewModel() {
	private val _rows = MutableStateFlow(SeerrDiscoverRow.entries.associateWith { SeerrRowState() })
	val rows: StateFlow<Map<SeerrDiscoverRow, SeerrRowState>> = _rows.asStateFlow()

	private val _myRequests = MutableStateFlow(SeerrRowState())
	val myRequests = _myRequests.asStateFlow()

	private val _searchResults = MutableStateFlow<SeerrRowState?>(null)
	val searchResults = _searchResults.asStateFlow()

	private val _signedOut = MutableStateFlow(false)
	val signedOut = _signedOut.asStateFlow()

	private var searchQuery = ""
	private var searchJob: Job? = null

	init {
		SeerrDiscoverRow.entries.forEach(::loadMore)
		refreshMyRequests()
	}

	fun loadMore(row: SeerrDiscoverRow) {
		val state = _rows.value.getValue(row)
		if (!state.canLoadMore) return
		updateRow(row) { it.copy(loading = true) }
		viewModelScope.launch {
			val result = load { repository.discover(row, state.page + 1) }
			updateRow(row) { current ->
				result.fold(current::append) { current.copy(loading = false, error = it) }
			}
		}
	}

	fun refreshMyRequests() {
		_myRequests.update { it.copy(loading = true) }
		viewModelScope.launch {
			val result = load { repository.myRequests() }
			_myRequests.value = result.fold(
				{ SeerrRowState(cards = it, page = 1, totalPages = 1) },
				{ SeerrRowState(error = it) },
			)
		}
	}

	fun search(query: String) {
		searchJob?.cancel()
		searchQuery = query.trim()
		if (searchQuery.isEmpty()) {
			_searchResults.value = null
			return
		}
		_searchResults.value = SeerrRowState(loading = true)
		searchJob = viewModelScope.launch { loadSearchPage(1) }
	}

	fun loadMoreSearchResults() {
		val state = _searchResults.value ?: return
		if (!state.canLoadMore || searchJob?.isActive == true) return
		_searchResults.value = state.copy(loading = true)
		searchJob = viewModelScope.launch { loadSearchPage(state.page + 1) }
	}

	private suspend fun loadSearchPage(page: Int) {
		val query = searchQuery
		val result = load { repository.search(query, page) }
		if (query != searchQuery) return
		val current = _searchResults.value ?: SeerrRowState()
		_searchResults.value = result.fold(current::append) { current.copy(loading = false, error = it) }
	}

	private fun updateRow(row: SeerrDiscoverRow, transform: (SeerrRowState) -> SeerrRowState) {
		_rows.update { rows -> rows + (row to transform(rows.getValue(row))) }
	}

	/** Runs a request, turning failures into a message and noting an ended session. */
	private suspend fun <T> load(block: suspend () -> T): Result<T, String> = try {
		Result.Success(block())
	} catch (err: CancellationException) {
		throw err
	} catch (err: SeerrSignedOutException) {
		_signedOut.value = true
		Result.Failure(err.message.orEmpty())
	} catch (err: Exception) {
		Timber.w(err, "Seerr request failed")
		Result.Failure(err.message ?: err.javaClass.simpleName)
	}

	private sealed interface Result<out T, out E> {
		data class Success<T>(val value: T) : Result<T, Nothing>
		data class Failure<E>(val error: E) : Result<Nothing, E>

		fun <R> fold(onSuccess: (T) -> R, onFailure: (E) -> R): R = when (this) {
			is Success -> onSuccess(value)
			is Failure -> onFailure(error)
		}
	}
}
