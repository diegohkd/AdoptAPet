package com.mobdao.adoptapet.presentation

import com.mobdao.adoptapet.common.testutils.MainDispatcherRule
import com.mobdao.adoptapet.domain.usecases.onboarding.HasCompletedOnboardingUseCase
import com.mobdao.adoptapet.presentation.AdoptAPetUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.navigation.Destination.Home
import com.mobdao.adoptapet.presentation.navigation.Destination.Onboarding
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdoptAPetViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private val hasCompletedOnboardingUseCase: HasCompletedOnboardingUseCase = mockk()

    private val tested by lazy { AdoptAPetViewModel(hasCompletedOnboardingUseCase) }

    @Test
    fun `given checking if user has completed onboarding throws an exception when initialized then generic error dialog is shown`() =
        runTest {
            // given
            every { hasCompletedOnboardingUseCase.execute() } returns flow { throw RuntimeException() }

            // when
            tested
            advanceTimeBy(1100L)

            // then
            assertTrue(tested.uiState.value.isGenericErrorDialogVisible)
        }

    @Test
    fun `given checking if user has completed onboarding throws an exception when initialized then no start destination is set`() =
        runTest {
            // given
            every { hasCompletedOnboardingUseCase.execute() } returns flow { throw RuntimeException() }

            // when
            tested
            advanceTimeBy(1100L)

            // then
            assertNull(tested.uiState.value.startDestination)
        }

    @Test
    fun `given user has not completed onboarding when initialized then start destination is Onboarding screen`() =
        runTest {
            // given
            every { hasCompletedOnboardingUseCase.execute() } returns flowOf(false)

            // when
            tested
            advanceTimeBy(1100L)

            // then
            assertEquals(
                tested.uiState.value.startDestination,
                Onboarding,
            )
        }

    @Test
    fun `given user has completed onboarding when initialized then start destination is Home screen`() =
        runTest {
            // given
            every { hasCompletedOnboardingUseCase.execute() } returns flowOf(true)

            // when
            tested
            advanceTimeBy(1100L)

            // then
            assertEquals(
                tested.uiState.value.startDestination,
                Home,
            )
        }

    @Test
    fun `given generic error is visible when dismiss generic error dialog then the dialog is hidden`() =
        runTest {
            // given
            every { hasCompletedOnboardingUseCase.execute() } returns flow { throw RuntimeException() }
            tested
            advanceTimeBy(1100L)

            // when
            tested.onUiAction(action = DismissGenericErrorDialog)

            // then
            assertFalse(tested.uiState.value.isGenericErrorDialogVisible)
        }
}
