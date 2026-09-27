package org.jellyfin.androidtv.ui.playback

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentActivity
import org.jellyfin.androidtv.R
import timber.log.Timber

/**
 * Experimental in-app player for remote YouTube trailers.
 *
 * The standard YouTube IFrame player is intentionally left visible and unmodified. This keeps
 * YouTube attribution, advertisements, captions and playback controls available. Videos that do
 * not permit embedding can still be opened with the normal external Android handler.
 */
class EmbeddedTrailerActivity : FragmentActivity() {
	private lateinit var webView: WebView
	private lateinit var externalUrl: String

	@SuppressLint("SetJavaScriptEnabled")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		WindowCompat.setDecorFitsSystemWindows(window, false)
		WindowInsetsControllerCompat(window, window.decorView).apply {
			hide(WindowInsetsCompat.Type.systemBars())
			systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
		}

		val videoId = intent.getStringExtra(EXTRA_VIDEO_ID)
		if (videoId == null || !YOUTUBE_ID_PATTERN.matches(videoId)) {
			finish()
			return
		}
		externalUrl = "https://www.youtube.com/watch?v=$videoId"

		try {
			webView = WebView(this).apply {
				setBackgroundColor(Color.BLACK)
				isFocusable = true
				isFocusableInTouchMode = true
			// Keep WebView focus for YouTube's controls without Android's full-view highlight.
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					defaultFocusHighlightEnabled = false
				}
				webChromeClient = WebChromeClient()
				webViewClient = object : WebViewClient() {
					override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
						if (!request.isForMainFrame) return false

						if (request.url.toString() == externalUrl) {
							openExternalTrailer()
						}

						return true
					}
				}
				settings.javaScriptEnabled = true
				settings.domStorageEnabled = true
				settings.mediaPlaybackRequiresUserGesture = false
				settings.allowFileAccess = false
				settings.allowContentAccess = false
				loadDataWithBaseURL(
					PLAYER_BASE_URL,
					createPlayerHtml(videoId),
					"text/html",
					"UTF-8",
					null,
				)
			}
			setContentView(webView)
			webView.requestFocus()
		} catch (error: RuntimeException) {
			Timber.e(error, "Unable to create embedded YouTube trailer player")
			openExternalTrailer()
		}
	}

	override fun dispatchKeyEvent(event: KeyEvent): Boolean {
		if (!::webView.isInitialized) {
			return super.dispatchKeyEvent(event)
		}

		val command = trailerPlaybackCommand(event.keyCode)
			?: return super.dispatchKeyEvent(event)

		if (
			event.action == KeyEvent.ACTION_DOWN &&
			event.repeatCount == 0
		) {
			webView.evaluateJavascript(command, null)
		}

		return true
	}

	override fun onResume() {
		super.onResume()

		if (::webView.isInitialized) {
			webView.onResume()
		}
	}

	override fun onPause() {
		if (::webView.isInitialized) {
			webView.evaluateJavascript("pausePlayback()", null)
			webView.onPause()
		}

		super.onPause()
	}

	override fun onDestroy() {
		if (::webView.isInitialized) {
			(webView.parent as? ViewGroup)?.removeView(webView)
			webView.stopLoading()
			webView.destroy()
		}

		super.onDestroy()
	}

	private fun openExternalTrailer() {
		if (externalUrl.isNotBlank()) {
			try {
				startActivity(Intent(Intent.ACTION_VIEW, externalUrl.toUri()))
			} catch (error: RuntimeException) {
				Timber.w(error, "Unable to open external YouTube trailer")
				Toast.makeText(this, R.string.no_player_message, Toast.LENGTH_LONG).show()
			}
		}
		finish()
	}

	private fun createPlayerHtml(videoId: String) = """
		<!doctype html>
		<html>
		<head>
			<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1">
			<meta name="referrer" content="strict-origin-when-cross-origin">
			<style>
				html, body, #player { width:100%; height:100%; margin:0; padding:0; overflow:hidden; background:#000; }
				#player iframe:focus { outline:none; }
			</style>
		</head>
		<body>
			<div id="player"></div>
			<script src="https://www.youtube.com/iframe_api"></script>
			<script>
				let player;
				function onYouTubeIframeAPIReady() {
					player = new YT.Player('player', {
						videoId: '$videoId',
						playerVars: { autoplay: 1, controls: 1, fs: 1, playsinline: 1, rel: 0 },
						events: { onError: function() { window.location.href = '$externalUrl'; } }
					});
				}
				function togglePlayback() {
					if (!player || !player.getPlayerState) return;
					if (player.getPlayerState() === YT.PlayerState.PLAYING) player.pauseVideo();
					else player.playVideo();
				}
				function playPlayback() {
					if (player && player.playVideo) {
						player.playVideo();
					}
				}
				function pausePlayback() {
					if (player && player.pauseVideo) {
						player.pauseVideo();
					}
				}
				function seekBy(seconds) {
					if (!player || !player.getCurrentTime || !player.seekTo) {
						return;
					}

					const requested = player.getCurrentTime() + seconds;
					const duration = player.getDuration ? player.getDuration() : 0;

					const target = duration > 0
						? Math.min(duration, Math.max(0, requested))
						: Math.max(0, requested);

					player.seekTo(target, true);
				}
			</script>
		</body>
		</html>
	""".trimIndent()

	companion object {
		private const val EXTRA_VIDEO_ID = "video_id"
		private const val EXTRA_EXTERNAL_URL = "external_url"
		private const val PLAYER_BASE_URL = "https://github.com/Smotheredhope80/jellyfin-androidtv/"
		private val YOUTUBE_ID_PATTERN = Regex("^[A-Za-z0-9_-]{11}$")

		fun createIntent(context: Context, videoId: String, externalUrl: String) =
			Intent(context, EmbeddedTrailerActivity::class.java).apply {
				putExtra(EXTRA_VIDEO_ID, videoId)
				putExtra(EXTRA_EXTERNAL_URL, externalUrl)
			}
	}
}

internal fun trailerPlaybackCommand(keyCode: Int): String? =
	when (keyCode) {
		KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> "togglePlayback()"
		KeyEvent.KEYCODE_MEDIA_PLAY -> "playPlayback()"
		KeyEvent.KEYCODE_MEDIA_PAUSE -> "pausePlayback()"
		KeyEvent.KEYCODE_MEDIA_REWIND -> "seekBy(-10)"
		KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> "seekBy(10)"
		KeyEvent.KEYCODE_DPAD_LEFT -> "seekBy(-10)"
		KeyEvent.KEYCODE_DPAD_RIGHT -> "seekBy(10)"
		KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> "togglePlayback()"
		else -> null
	}
