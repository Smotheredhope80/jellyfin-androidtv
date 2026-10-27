package org.jellyfin.androidtv.data.seerr

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class SeerrHttpException(val code: Int, message: String) : Exception(message)

data class SeerrResponse(
	val body: String,
	val setCookies: List<String>,
)

/**
 * Minimal HTTP access to a Seerr server. The session cookie is passed in per call; the client never
 * stores cookies or credentials itself.
 */
class SeerrClient(
	private val httpClient: OkHttpClient = OkHttpClient.Builder()
		.connectTimeout(10, TimeUnit.SECONDS)
		.readTimeout(15, TimeUnit.SECONDS)
		.followRedirects(false)
		.build(),
) {
	companion object {
		val json = Json {
			ignoreUnknownKeys = true
			coerceInputValues = true
			explicitNulls = false
		}

		private val jsonMediaType = "application/json".toMediaType()
	}

	suspend fun get(
		baseUrl: String,
		path: String,
		cookie: String?,
		query: Map<String, String?> = emptyMap(),
	): SeerrResponse = execute(baseUrl, path, cookie, query, body = null)

	suspend fun post(
		baseUrl: String,
		path: String,
		cookie: String?,
		body: String,
	): SeerrResponse = execute(baseUrl, path, cookie, emptyMap(), body)

	private suspend fun execute(
		baseUrl: String,
		path: String,
		cookie: String?,
		query: Map<String, String?>,
		body: String?,
	): SeerrResponse = withContext(Dispatchers.IO) {
		val url = "$baseUrl/api/v1$path".toHttpUrlOrNull()?.newBuilder()?.apply {
			query.forEach { (key, value) -> if (value != null) addQueryParameter(key, value) }
		}?.build() ?: throw SeerrHttpException(0, "Invalid Seerr address")

		val request = Request.Builder()
			.url(url)
			.header("Accept", "application/json")
			.apply { if (cookie != null) header("Cookie", cookie) }
			.apply { if (body != null) post(body.toRequestBody(jsonMediaType)) }
			.build()

		httpClient.newCall(request).execute().use { response ->
			val text = response.body?.string().orEmpty()
			if (!response.isSuccessful) {
				val error = runCatching { json.decodeFromString<SeerrError>(text) }.getOrNull()
				val message = error?.message ?: error?.error ?: "HTTP ${response.code}"
				throw SeerrHttpException(response.code, message)
			}
			SeerrResponse(text, response.headers("Set-Cookie"))
		}
	}
}
