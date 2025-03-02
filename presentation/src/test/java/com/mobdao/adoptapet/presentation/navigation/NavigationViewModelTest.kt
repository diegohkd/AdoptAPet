package com.mobdao.adoptapet.presentation.navigation

import com.mobdao.adoptapet.common.testutils.MainDispatcherRule
import com.mobdao.adoptapet.domain.models.AnimalType
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.FilterScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.OnboardingToHomeScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.PetDetailsScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.PreviousScreen
import com.mobdao.adoptapet.presentation.screens.filter.FilterNavAction
import com.mobdao.adoptapet.presentation.screens.home.HomeNavAction
import com.mobdao.adoptapet.presentation.screens.onboarding.OnboardingNavAction
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsNavAction
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class NavigationViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private val tested = NavigationViewModel()

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
}
