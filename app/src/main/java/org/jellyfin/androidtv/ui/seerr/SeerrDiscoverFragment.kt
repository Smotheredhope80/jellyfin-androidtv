package org.jellyfin.androidtv.ui.seerr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.data.seerr.SeerrCard
import org.jellyfin.androidtv.data.seerr.SeerrDiscoverRow
import org.jellyfin.androidtv.ui.base.JellyfinTheme
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.base.button.Button
import org.jellyfin.androidtv.ui.navigation.Destinations
import org.jellyfin.androidtv.ui.navigation.NavigationRepository
import org.jellyfin.androidtv.ui.shared.toolbar.MainToolbar
import org.jellyfin.androidtv.ui.shared.toolbar.MainToolbarActiveButton
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class SeerrDiscoverFragment : Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	) = content {
		JellyfinTheme {
			SeerrDiscoverScreen()
		}
	}
}

@Composable
private fun SeerrDiscoverScreen() {
	val viewModel = koinViewModel<SeerrDiscoverViewModel>()
	val navigationRepository = koinInject<NavigationRepository>()
	val focusManager = LocalFocusManager.current

	val rows by viewModel.rows.collectAsState()
	val myRequests by viewModel.myRequests.collectAsState()
	val searchResults by viewModel.searchResults.collectAsState()
	val signedOut by viewModel.signedOut.collectAsState()

	var query by rememberSaveable { mutableStateOf("") }
	val firstCardFocus = remember { FocusRequester() }
	// Not saveable: after returning from details the recreated view needs focus again
	var initialFocusDone by remember { mutableStateOf(false) }

	val trending = rows.getValue(SeerrDiscoverRow.TRENDING)
	LaunchedEffect(trending.cards.isNotEmpty()) {
		// Start on the first trending card instead of the search field so the keyboard stays closed
		if (!initialFocusDone && trending.cards.isNotEmpty()) {
			initialFocusDone = true
			firstCardFocus.requestFocus()
		}
	}

	val openCard: (SeerrCard) -> Unit = { card ->
		navigationRepository.navigate(Destinations.seerrDetails(card.type, card.tmdbId))
	}

	Column(Modifier.fillMaxSize().background(SeerrBackground)) {
		MainToolbar(MainToolbarActiveButton.Discover)

		if (signedOut) {
			Column(
				modifier = Modifier.padding(horizontal = 48.dp, vertical = 24.dp),
				verticalArrangement = Arrangement.spacedBy(14.dp),
			) {
				Text(stringResource(R.string.seerr_session_expired), color = Color.White, fontSize = 18.sp)
				Button(onClick = { navigationRepository.navigate(Destinations.seerrSetup, replace = true) }) {
					Text(stringResource(R.string.seerr_sign_in))
				}
			}
			return@Column
		}

		LazyColumn(
			contentPadding = PaddingValues(bottom = 48.dp),
			verticalArrangement = Arrangement.spacedBy(10.dp),
			modifier = Modifier.fillMaxSize(),
		) {
			item(key = "search") {
				SeerrTextField(
					value = query,
					onValueChange = { query = it },
					label = stringResource(R.string.lbl_search),
					placeholder = stringResource(R.string.seerr_search_hint),
					imeAction = ImeAction.Search,
					onImeAction = {
						viewModel.search(query)
						// Leave the field so TV keyboards close
						focusManager.moveFocus(FocusDirection.Down)
					},
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 48.dp, vertical = 8.dp),
				)
			}

			searchResults?.let { results ->
				item(key = "results") {
					SeerrCardRow(
						title = stringResource(R.string.seerr_row_results),
						cards = results.cards,
						message = rowMessage(results),
						onCardClick = openCard,
						onNearEnd = viewModel::loadMoreSearchResults,
					)
				}
			}

			if (myRequests.cards.isNotEmpty()) {
				item(key = "my-requests") {
					SeerrCardRow(
						title = stringResource(R.string.seerr_row_my_requests),
						cards = myRequests.cards,
						onCardClick = openCard,
					)
				}
			}

			SeerrDiscoverRow.entries.forEach { row ->
				val state = rows.getValue(row)
				item(key = row.name) {
					SeerrCardRow(
						title = stringResource(row.titleRes),
						cards = state.cards,
						message = rowMessage(state),
						onCardClick = openCard,
						onNearEnd = { viewModel.loadMore(row) },
						firstCardFocusRequester = if (row == SeerrDiscoverRow.TRENDING) firstCardFocus else null,
					)
				}
			}
		}
	}
}

private val SeerrDiscoverRow.titleRes
	get() = when (this) {
		SeerrDiscoverRow.TRENDING -> R.string.seerr_row_trending
		SeerrDiscoverRow.POPULAR_MOVIES -> R.string.seerr_row_popular_movies
		SeerrDiscoverRow.POPULAR_TV -> R.string.seerr_row_popular_tv
		SeerrDiscoverRow.UPCOMING_MOVIES -> R.string.seerr_row_upcoming_movies
	}

@Composable
private fun rowMessage(state: SeerrRowState): String = when {
	state.loading -> stringResource(R.string.loading)
	state.error != null -> stringResource(R.string.seerr_load_failed, state.error)
	else -> stringResource(R.string.seerr_no_results)
}
