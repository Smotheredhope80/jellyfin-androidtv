package org.jellyfin.androidtv.ui.playback

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
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
		externalUrl = intent.getStringExtra(EXTRA_EXTERNAL_URL).orEmpty()
		if (videoId == null || !YOUTUBE_ID_PATTERN.matches(videoId)) {
			finish()
			return
		}

		try {
			webView = WebView(this).apply {
				setBackgroundColor(Color.BLACK)
				isFocusable = true
				isFocusableInTouchMode = true
				webChromeClient = WebChromeClient()
				webViewClient = object : WebViewClient() {
					override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
						if (request.isForMainFrame) {
							startActivity(Intent(Intent.ACTION_VIEW, request.url))
							finish()
							return true
						}

						return false
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

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		if (!::webView.isInitialized) return super.onKeyDown(keyCode, event)

		val command = when (keyCode) {
			KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
			KeyEvent.KEYCODE_MEDIA_PLAY,
			KeyEvent.KEYCODE_MEDIA_PAUSE -> "togglePlayback()"
			KeyEvent.KEYCODE_MEDIA_REWIND -> "seekBy(-10)"
			KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> "seekBy(10)"
			else -> null
		}

		if (command != null) {
			webView.evaluateJavascript(command, null)
			return true
		}

		return super.onKeyDown(keyCode, event)
	}

	override fun onPause() {
		if (::webView.isInitialized) webView.evaluateJavascript("pausePlayback()", null)
		super.onPause()
	}

	override fun onDestroy() {
		if (::webView.isInitialized) {
			webView.stopLoading()
			webView.loadUrl("about:blank")
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
				function pausePlayback() { if (player && player.pauseVideo) player.pauseVideo(); }
				function seekBy(seconds) {
					if (player && player.getCurrentTime && player.seekTo) player.seekTo(player.getCurrentTime() + seconds, true);
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
