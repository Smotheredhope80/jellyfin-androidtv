package org.jellyfin.androidtv.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.compose.AndroidFragment
import androidx.fragment.compose.content
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.jellyfin.androidtv.auth.repository.ServerRepository
import org.jellyfin.androidtv.auth.repository.SessionRepository
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.data.repository.NotificationsRepository
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.preference.constant.AppTheme
import org.jellyfin.androidtv.ui.base.JellyfinTheme
import org.jellyfin.androidtv.ui.shared.toolbar.MainToolbar
import org.jellyfin.androidtv.ui.shared.toolbar.MainToolbarActiveButton
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {
	private val sessionRepository by inject<SessionRepository>()
	private val serverRepository by inject<ServerRepository>()
	private val notificationRepository by inject<NotificationsRepository>()
	private val userPreferences by inject<UserPreferences>()
	private val previewViewModel: HomePreviewViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	) = content {
		val elegantFin = userPreferences[UserPreferences.appTheme] == AppTheme.ELEGANT_FIN
		val rowsFocusRequester = remember { FocusRequester() }
		var rowsSupportFragment by remember { mutableStateOf<HomeRowsFragment?>(null) }
		var toolbarHeight by remember { mutableStateOf(95.dp) }
		val density = LocalDensity.current

		val inheritedColors = JellyfinTheme.colorScheme
		val colors = if (elegantFin) {
			inheritedColors.copy(
				background = colorResource(R.color.theme_elegantfin_background_dark),
				surface = colorResource(R.color.theme_elegantfin_surface),
				buttonFocused = colorResource(R.color.theme_elegantfin_accent),
				onButtonFocused = Color.White,
				rangeControlFill = colorResource(R.color.theme_elegantfin_accent),
			)
		} else {
			inheritedColors
		}

		LaunchedEffect(rowsFocusRequester) {
			rowsFocusRequester.requestFocus()
		}

		JellyfinTheme(colorScheme = colors) {
			Box(modifier = Modifier.fillMaxSize().then(if (elegantFin) Modifier.background(colors.background) else Modifier)) {
				if (elegantFin) {
					SelectedHomePreview(previewViewModel, toolbarHeight)
				}
				Column(Modifier.fillMaxSize()) {
					Box(Modifier.onSizeChanged { toolbarHeight = with(density) { it.height.toDp() } }) {
						MainToolbar(MainToolbarActiveButton.Home)
					}
					if (elegantFin) Spacer(Modifier.height(168.dp))

					// The leanback code has its own focus handling. This bridge only allows focus exit
					// when the current selected row is the first one, then resets Leanback state.
					AndroidFragment<HomeRowsFragment>(
						modifier = Modifier
							.focusGroup()
							.focusRequester(rowsFocusRequester)
							.clipToBounds()
							.focusProperties {
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
							.weight(1f)
							.fillMaxSize(),
						onUpdate = { fragment ->
							rowsSupportFragment = fragment
						},
					)
				}
			}
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		sessionRepository.currentSession
			.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
			.map { session ->
				if (session == null) null
				else serverRepository.getServer(session.serverId)
			}
			.onEach { server ->
				notificationRepository.updateServerNotifications(server)
			}
			.launchIn(viewLifecycleOwner.lifecycleScope)
	}
}

/** Keep selected-item recomposition outside the embedded Leanback fragment and its focus bridge. */
@Composable
private fun SelectedHomePreview(viewModel: HomePreviewViewModel, topInset: Dp) {
	val selectedItem by viewModel.selectedItem.collectAsState()
	ElegantFinHomePreview(item = selectedItem, topInset = topInset)
}
