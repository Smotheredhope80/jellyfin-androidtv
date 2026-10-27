package org.jellyfin.androidtv.ui.seerr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import kotlinx.coroutines.launch
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.auth.repository.UserRepository
import org.jellyfin.androidtv.data.seerr.SeerrRepository
import org.jellyfin.androidtv.ui.base.JellyfinTheme
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.base.button.Button
import org.jellyfin.androidtv.ui.navigation.Destinations
import org.jellyfin.androidtv.ui.navigation.NavigationRepository
import org.jellyfin.androidtv.ui.shared.toolbar.MainToolbar
import org.koin.compose.koinInject
import kotlin.coroutines.cancellation.CancellationException

class SeerrSetupFragment : Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	) = content {
		JellyfinTheme {
			SeerrSetupScreen()
		}
	}
}

@androidx.compose.runtime.Composable
private fun SeerrSetupScreen() {
	val repository = koinInject<SeerrRepository>()
	val userRepository = koinInject<UserRepository>()
	val navigationRepository = koinInject<NavigationRepository>()
	val context = LocalContext.current
	val resources = LocalResources.current
	val focusManager = LocalFocusManager.current
	val scope = rememberCoroutineScope()

	val session by repository.session.collectAsState()
	val currentUser by userRepository.currentUser.collectAsState()
	val signedIn = session != null && session?.jellyfinUserId == currentUser?.id?.toString()

	var serverUrl by rememberSaveable { mutableStateOf(repository.lastServerUrl) }
	var username by rememberSaveable { mutableStateOf(currentUser?.name.orEmpty()) }
	// The password is kept only in this composition and is never saved or restored
	var password by remember { mutableStateOf("") }
	var busy by remember { mutableStateOf(false) }
	var error by remember { mutableStateOf<String?>(null) }
	val firstFocus = remember { FocusRequester() }

	LaunchedEffect(signedIn) { firstFocus.requestFocus() }

	fun signIn() {
		val userId = currentUser?.id?.toString() ?: return
		if (busy || serverUrl.isBlank() || username.isBlank()) return
		busy = true
		error = null
		scope.launch {
			try {
				val user = repository.signIn(serverUrl, username.trim(), password, userId)
				password = ""
				Toast.makeText(context, resources.getString(R.string.pref_seerr_signed_in, user.label), Toast.LENGTH_SHORT).show()
				navigationRepository.navigate(Destinations.seerrDiscover, replace = true)
			} catch (err: CancellationException) {
				throw err
			} catch (err: Exception) {
				password = ""
				error = resources.getString(R.string.seerr_sign_in_failed, err.message ?: err.javaClass.simpleName)
			} finally {
				busy = false
			}
		}
	}

	Column(Modifier.fillMaxSize().background(SeerrBackground)) {
		MainToolbar()

		Column(
			modifier = Modifier
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = 48.dp, vertical = 24.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Column(
				modifier = Modifier.widthIn(max = 560.dp).fillMaxWidth(),
				verticalArrangement = Arrangement.spacedBy(14.dp),
			) {
				Text(stringResource(R.string.pref_seerr), color = Color.White, fontSize = 26.sp)

				if (signedIn) {
					Text(
						stringResource(R.string.seerr_signed_in_at, session?.seerrUserName.orEmpty(), session?.serverUrl.orEmpty()),
						color = Color.LightGray,
						fontSize = 14.sp,
					)
					Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
						Button(
							onClick = { navigationRepository.navigate(Destinations.seerrDiscover, replace = true) },
							modifier = Modifier.focusRequester(firstFocus),
						) { Text(stringResource(R.string.seerr_open_discover)) }
						Button(onClick = { scope.launch { repository.signOut() } }) {
							Text(stringResource(R.string.lbl_sign_out))
						}
					}
				} else {
					Text(stringResource(R.string.seerr_setup_description), color = Color.LightGray, fontSize = 14.sp)
					SeerrTextField(
						value = serverUrl,
						onValueChange = { serverUrl = it },
						label = stringResource(R.string.seerr_server_address),
						placeholder = stringResource(R.string.seerr_server_address_hint),
						keyboardType = KeyboardType.Uri,
						onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
						modifier = Modifier.fillMaxWidth().focusRequester(firstFocus),
					)
					SeerrTextField(
						value = username,
						onValueChange = { username = it },
						label = stringResource(R.string.seerr_username),
						onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
						modifier = Modifier.fillMaxWidth(),
					)
					SeerrTextField(
						value = password,
						onValueChange = { password = it },
						label = stringResource(R.string.seerr_password),
						password = true,
						imeAction = ImeAction.Done,
						onImeAction = {
							// Move focus off the field so TV keyboards close before signing in
							focusManager.moveFocus(FocusDirection.Down)
							signIn()
						},
						modifier = Modifier.fillMaxWidth(),
					)
					Button(
						onClick = { signIn() },
						enabled = !busy && serverUrl.isNotBlank() && username.isNotBlank(),
					) {
						Text(stringResource(if (busy) R.string.seerr_signing_in else R.string.seerr_sign_in))
					}
					error?.let { Text(it, color = Color(0xFFFCA5A5), fontSize = 14.sp) }
				}
			}
		}
	}
}
