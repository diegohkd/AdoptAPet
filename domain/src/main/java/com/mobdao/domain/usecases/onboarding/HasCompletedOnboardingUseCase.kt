package com.mobdao.domain.usecases.onboarding

import com.mobdao.domain.dataapi.services.OnboardingService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HasCompletedOnboardingUseCase
    @Inject
    internal constructor(
        private val onboardingService: OnboardingService,
    ) {
        fun execute(): Flow<Boolean> =
            flow {
                emit(onboardingService.hasCompletedOnboarding())
            }
    }
