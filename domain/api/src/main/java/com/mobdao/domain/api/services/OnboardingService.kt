package com.mobdao.domain.api.services

interface OnboardingService {

    fun hasCompletedOnboarding(): Boolean
    fun saveOnboardingAsCompleted()
}