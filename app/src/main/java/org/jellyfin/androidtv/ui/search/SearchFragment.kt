package org.jellyfin.androidtv.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.preference.constant.AppTheme
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.browsing.SelectedItemPreview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.compose.AndroidFragment
import androidx.fragment.compose.content
import androidx.leanback.app.RowsSupportFragment
import org.jellyfin.androidtv.ui.search.composable.SearchTextInput
import org.jellyfin.androidtv.ui.search.composable.SearchVoiceInput
import org.jellyfin.androidtv.ui.shared.toolbar.MainToolbar
import org.jellyfin.androidtv.ui.shared.toolbar.MainToolbarActiveButton
import org.jellyfin.androidtv.util.speech.rememberSpeechRecognizerAvailability
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

class SearchFragment : Fragment() {
	companion object {
		const val EXTRA_QUERY = "query"
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	) = content {
		val viewModel = koinViewModel<SearchViewModel>()
		val searchFragmentDelegate = koinInject<SearchFragmentDelegate> { parametersOf(requireContext()) }
		var query by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
		val textInputFocusRequester = remember { FocusRequester() }
		val resultFocusRequester = remember { FocusRequester() }
		val speechRecognizerAvailability = rememberSpeechRecognizerAvailability()
		val preferences = koinInject<UserPreferences>()
		val modern = preferences[UserPreferences.appTheme] == AppTheme.ELEGANT_FIN
		val selectedItem by searchFragmentDelegate.selectedItem.collectAsState()
		val results by viewModel.searchResultsFlow.collectAsState()
		val loading by viewModel.loading.collectAsState()
		val failedGroups by viewModel.failedGroups.collectAsState()
		var inputVisible by remember { mutableStateOf(true) }
		var editRequest by remember { mutableStateOf(0) }
		val searchButtonFocusRequester = remember { FocusRequester() }
		val keyboard = LocalSoftwareKeyboardController.current
		val showInput = !modern || inputVisible

		LaunchedEffect(editRequest) {
			if (editRequest > 0) {
				// Wait until the restored field has a focus node before requesting the keyboard.
				withFrameNanos { }
				textInputFocusRequester.requestFocus()
			}
		}

		LaunchedEffect(Unit) {
			val extraQuery = arguments?.getString(EXTRA_QUERY)
			if (!extraQuery.isNullOrBlank()) {
				query = query.copy(text = extraQuery)
				viewModel.searchImmediately(extraQuery)
				resultFocusRequester.requestFocus()
			} else {
				textInputFocusRequester.requestFocus()
			}

			viewModel.searchResultsFlow.collect { results ->
				searchFragmentDelegate.showResults(results)
			}
		}

		Column(Modifier.fillMaxSize().then(if (modern) Modifier.background(Color(0xFF0F172A)) else Modifier)) {
			MainToolbar(
				MainToolbarActiveButton.Search,
				onSearchClick = if (modern) ({ inputVisible = true; editRequest++ }) else null,
				searchFocusRequester = if (modern) searchButtonFocusRequester else null,
			)

			if (showInput) {
				Row(
					horizontalArrangement = Arrangement.spacedBy(12.dp),
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.focusRestorer()
						.focusGroup()
						.padding(horizontal = 48.dp)
				) {
					if (speechRecognizerAvailability) {
						SearchVoiceInput(
							onQueryChange = { query = query.copy(text = it) },
							onQuerySubmit = {
								viewModel.searchImmediately(query.text)
								resultFocusRequester.requestFocus()
							}
						)
					}

					SearchTextInput(
						query = query.text,
						onQueryChange = {
							query = query.copy(text = it)
							viewModel.searchDebounced(query.text)
						},
						onQuerySubmit = {
							viewModel.searchImmediately(query.text)
							// Note: We MUST change the focus to somewhere else when the keyboard is submitted because some vendors (like Amazon)
							// will otherwise just keep showing a (fullscreen) keyboard, soft-locking the app.
							resultFocusRequester.requestFocus()
						},
						modifier = Modifier
							.weight(1f)
							.focusRequester(textInputFocusRequester),
						modern = modern,
					)
				}
			}

			if (modern) {
				val count = results.sumOf { it.items.size }
				val status = when {
					query.text.isBlank() -> stringResource(R.string.search_start_hint)
					loading -> stringResource(R.string.loading)
					failedGroups > 0 && count == 0 -> stringResource(R.string.search_failed_hint)
					failedGroups > 0 -> stringResource(R.string.search_partial_hint, count)
					count == 0 -> stringResource(R.string.search_empty_hint)
					else -> stringResource(R.string.search_results_shown, count)
				}
				if (showInput) Text(status, color = Color(0xFFC4B5FD), fontSize = 12.sp, maxLines = 1,
					modifier = Modifier.padding(horizontal = 48.dp, vertical = 6.dp))
				Box(Modifier.fillMaxWidth().height(if (showInput) 128.dp else 184.dp)) {
					SelectedItemPreview(if (showInput) stringResource(R.string.lbl_search) else query.text,
						selectedItem, compact = showInput)
				}
			}

			// The leanback code has its own awful focus handling that doesn't work properly with Compose view inteop to workaround this
			// issue we add custom behavior that only allows focus exit when the current selected row is the first one. Additionally when
			// we do switch the focus, we reset the leanback state so it won't cause weird behavior when focus is regained
			var rowsSupportFragment by remember { mutableStateOf<RowsSupportFragment?>(null) }

			AndroidFragment<RowsSupportFragment>(
				modifier = Modifier
					.onFocusChanged {
						if (modern && it.hasFocus && results.any { group -> group.items.isNotEmpty() }) {
							inputVisible = false
							keyboard?.hide()
						}
					}
					.focusGroup()
					.focusRequester(resultFocusRequester)
					.focusProperties {
						if (modern && !showInput) up = searchButtonFocusRequester
						onExit = {
							val isFirstRowSelected = rowsSupportFragment?.selectedPosition?.let { it <= 0 } ?: false
							if (requestedFocusDirection != FocusDirection.Up || !isFirstRowSelected) {
								cancelFocusChange()
							} else {
								rowsSupportFragment?.selectedPosition = 0
								rowsSupportFragment?.verticalGridView?.clearFocus()
							}
						}
					}
					.padding(top = 5.dp)
					.fillMaxSize(),
				onUpdate = { fragment ->
					rowsSupportFragment = fragment
					if (modern) {
						// Anchor each selected row below its heading and focus-zoom clearance.
						// Percentage centering can lift tall poster rows above this short viewport.
						fragment.setAlignment((36 * resources.displayMetrics.density).toInt())
					}
					if (fragment.adapter !== searchFragmentDelegate.rowsAdapter) fragment.adapter = searchFragmentDelegate.rowsAdapter
					fragment.onItemViewClickedListener = searchFragmentDelegate.onItemViewClickedListener
					fragment.onItemViewSelectedListener = searchFragmentDelegate.onItemViewSelectedListener
				}
			)
		}
	}
}
