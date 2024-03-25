package com.mobdao.domain.usecases.onboarding

import com.mobdao.domain.api.services.OnboardingService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HasCompletedOnboardingUseCase @Inject constructor(
    private val onboardingService: OnboardingService
) {

    fun execute(): Flow<Boolean> = flow {
        emit(onboardingService.hasCompletedOnboarding())
    }
}