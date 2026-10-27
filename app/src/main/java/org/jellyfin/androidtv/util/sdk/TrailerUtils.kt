package org.jellyfin.androidtv.util.sdk

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import org.jellyfin.androidtv.ui.playback.EmbeddedTrailerActivity
import org.jellyfin.sdk.model.api.BaseItemDto
import java.net.URI
import java.net.URLDecoder

object TrailerUtils {
	private const val YOUTUBE_HOST = "youtube.com"
	private const val YOUTUBE_SHORT_HOST = "youtu.be"
	private const val YOUTUBE_ID_PARAMETER = "v"
	private const val YOUTUBE_NFO_ID_PARAMETER = "videoid"
	private const val YOUTUBE_URL = "https://youtube.com/watch?v="
	private const val YOUTUBE_ID_LENGTH = 11
	private const val FRAMEWORK_STUB_PACKAGE = "com.android.tv.frameworkpackagestubs"
	private val YOUTUBE_ID_PATTERN = Regex("^[A-Za-z0-9_-]{$YOUTUBE_ID_LENGTH}$")

	internal fun getYoutubeVideoId(src: String): String? {
		val uri = runCatching { URI(src) }.getOrNull() ?: return null
		val host = uri.host?.lowercase().orEmpty().removePrefix("www.").removePrefix("m.")
		val pathSegments = uri.path.orEmpty().split('/').filter(String::isNotBlank)
		val query = uri.rawQuery.orEmpty()
			.split('&')
			.mapNotNull { parameter ->
				val separator = parameter.indexOf('=')
				if (separator < 0) null
				else URLDecoder.decode(parameter.substring(0, separator), "UTF-8") to
					URLDecoder.decode(parameter.substring(separator + 1), "UTF-8")
			}
			.toMap()
		val isYoutubeHost = host == YOUTUBE_HOST || host.endsWith(".$YOUTUBE_HOST")
		val id = when {
			host == YOUTUBE_SHORT_HOST -> pathSegments.firstOrNull()
			isYoutubeHost && pathSegments.firstOrNull() in setOf("embed", "shorts", "live") ->
				pathSegments.getOrNull(1)
			isYoutubeHost -> query[YOUTUBE_ID_PARAMETER]
			uri.scheme == "plugin" -> query[YOUTUBE_NFO_ID_PARAMETER]
			else -> null
		}

		return id?.takeIf(YOUTUBE_ID_PATTERN::matches)
	}

	@JvmStatic
	fun getExternalTrailerIntent(src: String): Intent {
		val uri = src.toUri()

		// Recreate YouTube urls
		getYoutubeVideoId(src)?.let { id ->
			return Intent(Intent.ACTION_VIEW, "$YOUTUBE_URL$id".toUri())
		}

		return Intent(Intent.ACTION_VIEW, uri)
	}

	private fun getTrailerIntent(context: Context, src: String): Intent {
		val youtubeVideoId = getYoutubeVideoId(src)
		return if (youtubeVideoId != null) {
			EmbeddedTrailerActivity.createIntent(context, youtubeVideoId, "$YOUTUBE_URL$youtubeVideoId")
		} else {
			getExternalTrailerIntent(src)
		}
	}

	/** Opens a YouTube video by its key, using the same embedded player and fallback as item trailers. */
	@JvmStatic
	fun getYoutubeTrailerIntent(context: Context, videoKey: String): Intent = getTrailerIntent(context, "$YOUTUBE_URL$videoKey")

	@JvmStatic
	fun getExternalTrailerIntent(context: Context, item: BaseItemDto): Intent? = item.remoteTrailers.orEmpty()
		.mapNotNull { it.url?.let { url -> getTrailerIntent(context, url) } }
		.firstOrNull {
			if (it.component != null) return@firstOrNull true
			val component = it.resolveActivity(context.packageManager)

			// Check if there is an activity to handle the intent
			// exclude the FrameworkPackageStubs module, which only displays a message
			// that there is no app to open the intent
			component != null && component.packageName != FRAMEWORK_STUB_PACKAGE
		}

	@JvmStatic
	fun hasPlayableTrailers(context: Context, item: BaseItemDto): Boolean {
		// Local trailer
		if (item.localTrailerCount != null && item.localTrailerCount!! > 0) return true

		// External trailer
		if (getExternalTrailerIntent(context, item) != null) return true

		// No trailer found
		return false
	}
}
