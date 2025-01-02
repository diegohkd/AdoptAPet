package com.mobdao.adoptapet.presentation.navigation

import com.mobdao.adoptapet.domain.models.AnimalType
import com.mobdao.adoptapet.domain.usecases.onboarding.HasCompletedOnboardingUseCase
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.FilterScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.OnboardingScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.OnboardingToHomeScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.PetDetailsScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.PreviousScreen
import com.mobdao.adoptapet.presentation.screens.filter.FilterNavAction
import com.mobdao.adoptapet.presentation.screens.home.HomeNavAction
import com.mobdao.adoptapet.presentation.screens.onboarding.OnboardingNavAction
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsNavAction
import com.mobdao.adoptapet.presentation.screens.splash.SplashViewModel
import com.mobdao.common.testutils.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NavigationViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private val hasCompletedOnboardingUseCase: HasCompletedOnboardingUseCase = mockk()

    private val tested = NavigationViewModel(hasCompletedOnboardingUseCase)

    @Test
    fun `given nav action is Completed and checking if user has completed onboarding throws an exception when Splash nav action received then generic error dialog is shown`() {
        // given
        every { hasCompletedOnboardingUseCase.execute() } returns flow { throw RuntimeException() }
        val navAction = SplashViewModel.NavAction.Completed

        // when
        tested.onNavAction(navAction)

        // then
        assertTrue(tested.uiState.value.genericErrorDialogIsVisible)
    }

    @Test
    fun `given nav action is Completed and checking if user has completed onboarding throws an exception when Splash nav action received then no navigation is executed`() {
        // given
        every { hasCompletedOnboardingUseCase.execute() } returns flow { throw RuntimeException() }
        val navAction = SplashViewModel.NavAction.Completed

        // when
        tested.onNavAction(navAction)

        // then
        assertNull(tested.navAction.value)
    }

    @Test
    fun `given nav action is Completed and user has not completed onboarding when Splash nav action received then navigate to Onboarding screen`() {
        // given
        every { hasCompletedOnboardingUseCase.execute() } returns flowOf(false)
        val navAction = SplashViewModel.NavAction.Completed

        // when
        tested.onNavAction(navAction)

        // then
        assertEquals(
            tested.navAction.value!!.peekContent(),
            OnboardingScreen,
        )
    }

    @Test
    fun `given nav action is Completed and user has completed onboarding when Splash nav action received then navigate to Home screen`() {
        // given
        every { hasCompletedOnboardingUseCase.execute() } returns flowOf(false)
        val navAction = SplashViewModel.NavAction.Completed

        // when
        tested.onNavAction(navAction)

        // then
        assertEquals(
            tested.navAction.value!!.peekContent(),
            OnboardingScreen,
        )
    }

    @Test
    fun `given nav action is Completed when Onboarding nav action received then navigate to Home screen`() {
        // given
        val navAction = OnboardingNavAction.Completed

        // when
        tested.onNavAction(navAction)

        // then
        assertEquals(
            tested.navAction.value!!.peekContent(),
            OnboardingToHomeScreen,
        )
    }

    @Test
    fun `given nav action is PetClicked when Onboarding nav action received then navigate to Pet Details screen`() {
        // given
        val animalType = mockk<AnimalType>()
        val navAction = HomeNavAction.PetClicked(petId = "petId", type = animalType)

        // when
        tested.onNavAction(navAction)

        // then
        assertEquals(
            tested.navAction.value!!.peekContent(),
            PetDetailsScreen(
                petId = "petId",
                type = animalType,
            ),
        )
    }

    @Test
    fun `given nav action is FilterClicked when Onboarding nav action received then navigate to Filter screen`() {
        // given
        val navAction = HomeNavAction.FilterClicked

        // when
        tested.onNavAction(navAction)

        // then
        assertEquals(
            tested.navAction.value!!.peekContent(),
            FilterScreen,
        )
    }

    @Test
    fun `given nav action is BackButtonClicked when Pet Details nav action received then navigate to PreviousScreen screen`() {
        // given
        val navAction = PetDetailsNavAction.BackButtonClicked

        // when
        tested.onNavAction(navAction)

        // then
        assertEquals(
            tested.navAction.value!!.peekContent(),
            PreviousScreen,
        )
    }

    @Test
    fun `given nav action is FilterApplied when Filter nav action received then navigate to PreviousScreen screen`() {
        // given
        val navAction = FilterNavAction.FilterApplied

        // when
        tested.onNavAction(navAction)

        // then
        assertEquals(
            tested.navAction.value!!.peekContent(),
            PreviousScreen,
        )
    }

    @Test
    fun `given generic error is visible when dismiss generic error dialog then the dialog is hidden`() {
        // given
        every { hasCompletedOnboardingUseCase.execute() } returns flow { throw RuntimeException() }
        val navAction = SplashViewModel.NavAction.Completed
        tested.onNavAction(navAction)

        // when
        tested.onDismissGenericErrorDialog()

        // then
        assertFalse(tested.uiState.value.genericErrorDialogIsVisible)
    }
}
