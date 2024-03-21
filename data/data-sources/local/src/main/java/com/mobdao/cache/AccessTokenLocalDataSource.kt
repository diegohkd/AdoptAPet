package com.mobdao.cache

import android.content.SharedPreferences
import timber.log.Timber

private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"

class AccessTokenLocalDataSource internal constructor(private val sharedPreferences: SharedPreferences) {

    fun getAccessToken(): String? =
        try {
            sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        } catch (exception: Exception) {
            Timber.e(exception)
            null
        }

    fun saveAccessToken(accessToken: String?) {
        sharedPreferences.edit()
            .putString(ACCESS_TOKEN_KEY, accessToken)
            .apply()
    }
}
