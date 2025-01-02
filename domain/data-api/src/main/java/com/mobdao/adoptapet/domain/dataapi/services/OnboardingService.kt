package com.mobdao.adoptapet.domain.dataapi.services

interface OnboardingService {
    fun hasCompletedOnboarding(): Boolean

    fun saveOnboardingAsCompleted()
}
