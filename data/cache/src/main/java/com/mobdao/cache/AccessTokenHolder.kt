package com.mobdao.cache

import android.content.SharedPreferences

private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"

class AccessTokenHolder(private val sharedPreferences: SharedPreferences) {

    fun get(): String? =
        try {
            sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        } catch (e: Exception) {
            null
        }

    fun save(accessToken: String?) {
        sharedPreferences.edit()
            .putString(ACCESS_TOKEN_KEY, accessToken)
            .apply()
    }
}