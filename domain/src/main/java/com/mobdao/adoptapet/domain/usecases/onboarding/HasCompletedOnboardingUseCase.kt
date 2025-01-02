package com.mobdao.adoptapet.domain.usecases.onboarding

import com.mobdao.adoptapet.domain.dataapi.services.OnboardingService
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
