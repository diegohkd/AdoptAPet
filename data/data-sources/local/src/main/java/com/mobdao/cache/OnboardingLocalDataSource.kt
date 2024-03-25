package com.mobdao.cache

import android.content.SharedPreferences
import timber.log.Timber

private const val HAS_COMPLETED_ONBOARDING = "HAS_COMPLETED_ONBOARDING"

class OnboardingLocalDataSource internal constructor(
    private val sharedPreferences: SharedPreferences,
) {

    fun hasCompletedOnboarding(): Boolean =
        try {
            sharedPreferences.getBoolean(HAS_COMPLETED_ONBOARDING, false)
        } catch (exception: Exception) {
            Timber.e(exception)
            false
        }

    fun completeOnboarding() {
        sharedPreferences.edit()
            .putBoolean(HAS_COMPLETED_ONBOARDING, true)
            .apply()
    }
}