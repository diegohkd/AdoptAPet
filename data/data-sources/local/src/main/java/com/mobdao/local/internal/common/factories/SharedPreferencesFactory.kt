package com.mobdao.local.internal.common.factories

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

private const val ACCESS_TOKEN_PREFERENCES_KEY = "ACCESS_TOKEN_PREFERENCES_KEY"
private const val ONBOARDING_PREFERENCES_KEY = "ONBOARDING_PREFERENCES_KEY"

@Singleton
internal class SharedPreferencesFactory
    @Inject
    constructor(
        private val context: Context,
    ) {
        fun createForAccessTokenDataSource(): SharedPreferences = create(ACCESS_TOKEN_PREFERENCES_KEY)

        fun createForOnboardingDataSource(): SharedPreferences = create(ONBOARDING_PREFERENCES_KEY)

        private fun create(key: String): SharedPreferences =
            context.getSharedPreferences(
                key,
                Context.MODE_PRIVATE,
            )
    }
