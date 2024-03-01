package com.mobdao.data.utils.factories

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

private const val ACCESS_TOKEN_HOLDER_PREFERENCES_KEY = "ACCESS_TOKEN_HOLDER_PREFERENCES_KEY"

@Singleton
class SharedPreferencesFactory @Inject constructor(private val context: Context) {

    fun createForAccessTokenHolder(): SharedPreferences =
        context.getSharedPreferences(ACCESS_TOKEN_HOLDER_PREFERENCES_KEY, MODE_PRIVATE)
}