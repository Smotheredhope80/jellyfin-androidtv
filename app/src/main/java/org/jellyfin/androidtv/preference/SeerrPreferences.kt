package org.jellyfin.androidtv.preference

import android.content.Context
import org.jellyfin.preference.intPreference
import org.jellyfin.preference.store.SharedPreferenceStore
import org.jellyfin.preference.stringPreference

/**
 * Seerr session for one Jellyfin user. Stored in its own file, which is deliberately absent from the
 * backup allow-lists (`backup_content.xml`, `backup_rules.xml`) because it holds a session cookie.
 * The Seerr password is never stored.
 */
class SeerrPreferences(context: Context) : SharedPreferenceStore(
	sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
) {
	companion object {
		const val SHARED_PREFERENCES_NAME = "seerr_session"

		val serverUrl = stringPreference("server_url", "")
		val sessionCookie = stringPreference("session_cookie", "")
		val jellyfinUserId = stringPreference("jellyfin_user_id", "")
		val seerrUserId = intPreference("seerr_user_id", 0)
		val seerrUserName = stringPreference("seerr_user_name", "")
	}
}
