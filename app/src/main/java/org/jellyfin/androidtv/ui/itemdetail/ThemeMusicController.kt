package org.jellyfin.androidtv.ui.itemdetail

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.ui.playback.MediaManager
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.extensions.audioApi
import org.jellyfin.sdk.api.client.extensions.libraryApi
import org.jellyfin.sdk.api.client.util.AuthorizationHeaderBuilder
import org.jellyfin.sdk.model.api.BaseItemDto
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/** Browsing ambience only: no playback queue, media session, or watched/progress reports. */
class ThemeMusicController(private val fragment: Fragment) : DefaultLifecycleObserver, KoinComponent {
	private val api by inject<ApiClient>()
	private val preferences by inject<UserPreferences>()
	private val mediaManager by inject<MediaManager>()
	private val audio = fragment.requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
	private var item: BaseItemDto? = null
	private var job: Job? = null
	private var player: MediaPlayer? = null
	private var hasFocus = false
	private val gate = ThemeMusicGate()
	private val focusListener = AudioManager.OnAudioFocusChangeListener { change ->
		if (change != AudioManager.AUDIOFOCUS_GAIN) stop()
	}

	init { fragment.lifecycle.addObserver(this) }

	fun select(value: BaseItemDto?) {
		if (item?.id == value?.id) return
		stop()
		item = value
		start()
	}

	override fun onResume(owner: LifecycleOwner) = start()
	override fun onPause(owner: LifecycleOwner) = stop()
	override fun onDestroy(owner: LifecycleOwner) = dispose()

	private fun start() {
		val selected = item ?: return
		if (!preferences[UserPreferences.tvThemeMusic] || !selected.supportsThemeMusic()) return
		if (!fragment.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) return
		if (job != null || player != null || mediaManager.isPlayingAudio || audio.isMusicActive) return
		val ticket = gate.begin()
		job = fragment.lifecycleScope.launch {
			try {
				val theme = api.libraryApi.getThemeSongs(itemId = selected.id, inheritFromParent = true)
					.content.items.firstOrNull() ?: return@launch
				ensureActive()
				if (!gate.accepts(ticket) || mediaManager.isPlayingAudio || audio.isMusicActive) return@launch
				val token = api.accessToken ?: return@launch
				val url = api.audioApi.getAudioStreamUrl(itemId = theme.id, static = true)
				val headers = mapOf("Authorization" to AuthorizationHeaderBuilder.buildHeader(
					api.clientInfo.name, api.clientInfo.version, api.deviceInfo.id, api.deviceInfo.name, token,
				))
				@Suppress("DEPRECATION")
				val result = audio.requestAudioFocus(focusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
				if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) return@launch
				hasFocus = true
				val current = MediaPlayer()
				player = current
				current.setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
					.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
				current.setVolume(0.25f, 0.25f)
				current.setOnPreparedListener {
					if (gate.accepts(ticket) && player === it && fragment.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) it.start()
				}
				current.setOnCompletionListener { stop() }
				current.setOnErrorListener { _, _, _ -> stop(); true }
				current.setDataSource(fragment.requireContext().applicationContext, Uri.parse(url), headers)
				current.prepareAsync()
			} catch (cancelled: CancellationException) {
				throw cancelled
			} catch (_: Exception) {
				// Missing/unavailable/unsupported theme stays silent; never log authenticated URLs.
				stop()
			}
		}
	}

	fun stop() {
		gate.stop()
		job?.cancel()
		job = null
		player?.apply {
			setOnPreparedListener(null)
			setOnCompletionListener(null)
			setOnErrorListener(null)
			release()
		}
		player = null
		if (hasFocus) {
			hasFocus = false
			@Suppress("DEPRECATION")
			audio.abandonAudioFocus(focusListener)
		}
	}

	fun dispose() {
		stop()
		fragment.lifecycle.removeObserver(this)
	}
}
