package com.mobdao.domain.dataapi.services

interface OnboardingService {
    fun hasCompletedOnboarding(): Boolean

    fun saveOnboardingAsCompleted()
}
