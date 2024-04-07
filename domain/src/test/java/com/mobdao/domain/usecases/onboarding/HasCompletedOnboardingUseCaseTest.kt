package com.mobdao.domain.usecases.onboarding

import com.mobdao.domain.api.services.OnboardingService
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HasCompletedOnboardingUseCaseTest {

    private val onboardingService: OnboardingService = mockk {
        every { hasCompletedOnboarding() } returns false
    }

    private val tested = HasCompletedOnboardingUseCase(onboardingService)

    @Test
    fun `given has not completed onboarding when execute then return false`() = runTest {
        // given / when
        val result: Boolean = tested.execute().first()

        // then
        assertFalse(result)
    }

    @Test
    fun `given has completed onboarding when execute then return true`() = runTest {
        // given
        every { onboardingService.hasCompletedOnboarding() } returns true

        // when
        val result: Boolean = tested.execute().first()

        // then
        assertTrue(result)
    }
}