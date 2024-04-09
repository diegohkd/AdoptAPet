package com.mobdao.cache

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingLocalDataSourceTest {

    private val editor: Editor = mockk {
        every { putBoolean("HAS_COMPLETED_ONBOARDING", true) } returns this
        justRun { apply() }
    }

    private val sharedPreferences: SharedPreferences = mockk {
        every { getBoolean("HAS_COMPLETED_ONBOARDING", false) } returns false
        every { edit() } returns editor
    }

    private val tested = OnboardingLocalDataSource(sharedPreferences)

    @Test
    fun `given getting if has completed onboarding throws an exception when check if has completed onboarding then false is returned`() {
        // given
        every {
            sharedPreferences.getBoolean(
                "HAS_COMPLETED_ONBOARDING",
                false
            )
        } throws RuntimeException()

        // when
        val result = tested.hasCompletedOnboarding()

        // then
        assertFalse(result)
    }

    @Test
    fun `given getting if has completed onboarding returns true when check if has completed onboarding then true is returned`() {
        // given
        every {
            sharedPreferences.getBoolean(
                "HAS_COMPLETED_ONBOARDING",
                false
            )
        } returns true

        // when
        val result = tested.hasCompletedOnboarding()

        // then
        assertTrue(result)
    }

    @Test
    fun `given getting if has completed onboarding returns false when check if has completed onboarding then false is returned`() {
        // given / when
        val result = tested.hasCompletedOnboarding()

        // then
        assertFalse(result)
    }

    @Test
    fun `when complete onboarding then persisted flag is saved as true`() {
        // when
        tested.completeOnboarding()

        // then
        verifyOrder {
            sharedPreferences.edit()
            editor.putBoolean("HAS_COMPLETED_ONBOARDING", true)
            editor.apply()
        }
    }
}