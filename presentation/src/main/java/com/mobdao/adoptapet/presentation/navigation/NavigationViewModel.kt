package com.mobdao.adoptapet.presentation.navigation

import androidx.lifecycle.ViewModel
import com.mobdao.adoptapet.domain.models.AnimalType
import com.mobdao.adoptapet.presentation.common.Event
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.FilterScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.OnboardingToHomeScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.PetDetailsScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.PreviousScreen
import com.mobdao.adoptapet.presentation.screens.filter.FilterNavAction
import com.mobdao.adoptapet.presentation.screens.home.HomeNavAction
import com.mobdao.adoptapet.presentation.screens.home.HomeNavAction.FilterClicked
import com.mobdao.adoptapet.presentation.screens.home.HomeNavAction.PetClicked
import com.mobdao.adoptapet.presentation.screens.onboarding.OnboardingNavAction
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsNavAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {
    sealed interface NavAction {
        data object OnboardingToHomeScreen : NavAction

        data object FilterScreen : NavAction

        data class PetDetailsScreen(
            val petId: String,
            val type: AnimalType,
        ) : NavAction

        data object PreviousScreen : NavAction
    }

    data class UiState(
        val genericErrorDialogIsVisible: Boolean = false,
    )

    private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
    val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onNavAction(navAction: OnboardingNavAction) {
        when (navAction) {
            OnboardingNavAction.Completed -> {
                _navAction.value = Event(OnboardingToHomeScreen)
            }
        }
    }

    fun onNavAction(navAction: HomeNavAction) {
        _navAction.value =
            Event(
                when (navAction) {
                    is PetClicked ->
                        PetDetailsScreen(
                            petId = navAction.petId,
                            type = navAction.type,
                        )
                    FilterClicked -> FilterScreen
                },
            )
    }

    fun onNavAction(navAction: PetDetailsNavAction) {
        _navAction.value =
            Event(
                when (navAction) {
                    is PetDetailsNavAction.BackButtonClicked -> PreviousScreen
                },
            )
    }

    fun onNavAction(navAction: FilterNavAction) {
        when (navAction) {
            FilterNavAction.BackClicked -> {
                _navAction.value = Event(PreviousScreen)
            }
            FilterNavAction.FilterApplied -> {
                _navAction.value = Event(PreviousScreen)
            }
        }
    }

    fun onDismissGenericErrorDialog() {
        _uiState.update { it.copy(genericErrorDialogIsVisible = false) }
    }
}
