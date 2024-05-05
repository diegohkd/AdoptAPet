package com.mobdao.data.services

import com.mobdao.domain.dataapi.services.OnboardingService
import com.mobdao.local.OnboardingLocalDataSource
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
