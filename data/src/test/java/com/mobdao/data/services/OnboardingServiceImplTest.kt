package com.mobdao.data.services

import com.mobdao.local.OnboardingLocalDataSource
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingServiceImplTest {

    private val onboardingLocalDataSource: OnboardingLocalDataSource = mockk {
        justRun { completeOnboarding() }
    }

    private val tested = OnboardingServiceImpl(onboardingLocalDataSource)

    @Test
    fun `given has not completed onboarding when check if has completed then false is returned`() {
        // given
        every { onboardingLocalDataSource.hasCompletedOnboarding() } returns false

        // when
        val result = tested.hasCompletedOnboarding()

        // then
        assertFalse(result)
    }

    @Test
    fun `given has completed onboarding when check if has completed then true is returned`() {
        // given
        every { onboardingLocalDataSource.hasCompletedOnboarding() } returns true

        // when
        val result = tested.hasCompletedOnboarding()

        // then
        assertTrue(result)
    }

    @Test
    fun `when save onboarding as completed then onboarding is completed`() {
        // when
        tested.saveOnboardingAsCompleted()

        // then
        verify { onboardingLocalDataSource.completeOnboarding() }
    }
}
