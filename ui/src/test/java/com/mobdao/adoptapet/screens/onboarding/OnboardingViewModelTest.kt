package com.mobdao.adoptapet.screens.onboarding

import com.mobdao.adoptapet.screens.onboarding.OnboardingViewModel.NavAction.Completed
import com.mobdao.common.testutils.MainDispatcherRule
import com.mobdao.common.testutils.mockfactories.domain.AddressMockFactory
import com.mobdao.domain.models.Address
import com.mobdao.domain.usecases.onboarding.CompleteOnboardingUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class OnboardingViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private val address: Address = AddressMockFactory.create()

    private val completedOnboardingUseCase: CompleteOnboardingUseCase = mockk {
        every { execute(address) } returns flowOf(Unit)
    }

    private val tested by lazy {
        OnboardingViewModel(
            completedOnboardingUseCase = completedOnboardingUseCase,
        )
    }

    @Test
    fun `when failed to get address then generic error dialog is visible`() {
        // when
        tested.onFailedToGetAddress(throwable = null)

        // then
        assertTrue(tested.uiState.value.genericErrorDialogIsVisible)
    }

    @Test
    fun `given address is not selected when initialized then next button is disabled`() {
        // given / when then
        assertFalse(tested.uiState.value.nextButtonIsEnabled)
    }

    @Test
    fun `when address selected then next button is visible`() {
        // given / when
        tested.onAddressSelected(address)

        // then
        assertTrue(tested.uiState.value.nextButtonIsEnabled)
    }

    @Test
    fun `given address selected and completing onboarding throws an exception when next clicked then generic error dialog is shown`() {
        // given
        every { completedOnboardingUseCase.execute(any()) } returns flow { throw Exception() }
        tested.onAddressSelected(address)

        // when
        tested.onNextClicked()

        // then
        assertTrue(tested.uiState.value.genericErrorDialogIsVisible)
    }

    @Test
    fun `given address selected and completing onboarding is successful when next clicked then Completed nav action is emitted`() {
        // given
        tested.onAddressSelected(address)

        // when
        tested.onNextClicked()

        // then
        assertEquals(
            tested.navAction.value!!.peekContent(),
            Completed,
        )
    }

    @Test
    fun `given generic error dialog is visible when on dismiss the dialog then it is hidden`() {
        // given
        tested.onFailedToGetAddress(throwable = null)

        // when
        tested.onDismissGenericErrorDialog()

        // then
        assertFalse(tested.uiState.value.genericErrorDialogIsVisible)
    }
}
