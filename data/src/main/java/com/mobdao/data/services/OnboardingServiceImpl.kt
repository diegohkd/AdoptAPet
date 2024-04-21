package com.mobdao.data.services

import com.mobdao.cache.OnboardingLocalDataSource
import com.mobdao.domain.dataapi.services.OnboardingService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingServiceImpl @Inject constructor(
    private val onboardingLocalDataSource: OnboardingLocalDataSource,
) : OnboardingService {

    override fun hasCompletedOnboarding(): Boolean =
        onboardingLocalDataSource.hasCompletedOnboarding()

    override fun saveOnboardingAsCompleted() {
        onboardingLocalDataSource.completeOnboarding()
    }
}