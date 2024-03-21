package com.mobdao.cache

import android.content.SharedPreferences

private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"

class AccessTokenLocalDataSource internal constructor(private val sharedPreferences: SharedPreferences) {

    fun getAccessToken(): String? =
        try {
            sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        } catch (e: Exception) {
            null
        }

    fun saveAccessToken(accessToken: String?) {
        sharedPreferences.edit()
            .putString(ACCESS_TOKEN_KEY, accessToken)
            .apply()
    }
}