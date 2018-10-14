package th.co.bluesharp.smack.Utils

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.toolbox.Volley

class SharePref(context: Context) {
    val PREF_FILENAME = "pref"
    val pref: SharedPreferences = context.getSharedPreferences(PREF_FILENAME, 0)
    val IS_LOGGED_IN = "isLoggedIn"
    val AUTH_TOKEN = "authToken"
    val USER_EMAIL = "userEmail"

    var isLoggedIn: Boolean
        get() = pref.getBoolean(IS_LOGGED_IN, false)
        set(value) = pref.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var authToken: String
        get() = pref.getString(AUTH_TOKEN, "")
        set(value) = pref.edit().putString(AUTH_TOKEN, value).apply()

    var userEmail: String
        get() = pref.getString(USER_EMAIL, "")
        set(value) = pref.edit().putString(USER_EMAIL, value).apply()

    val requestQueue = Volley.newRequestQueue(context)
}