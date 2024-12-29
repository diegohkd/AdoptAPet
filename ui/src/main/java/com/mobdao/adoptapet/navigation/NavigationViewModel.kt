package com.mobdao.adoptapet.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.navigation.NavigationViewModel.NavAction.FilterScreen
import com.mobdao.adoptapet.navigation.NavigationViewModel.NavAction.OnboardingScreen
import com.mobdao.adoptapet.navigation.NavigationViewModel.NavAction.OnboardingToHomeScreen
import com.mobdao.adoptapet.navigation.NavigationViewModel.NavAction.PetDetailsScreen
import com.mobdao.adoptapet.navigation.NavigationViewModel.NavAction.PreviousScreen
import com.mobdao.adoptapet.navigation.NavigationViewModel.NavAction.SplashToHomeScreen
import com.mobdao.adoptapet.screens.filter.FilterNavAction
import com.mobdao.adoptapet.screens.home.HomeNavAction
import com.mobdao.adoptapet.screens.home.HomeNavAction.FilterClicked
import com.mobdao.adoptapet.screens.home.HomeNavAction.PetClicked
import com.mobdao.adoptapet.screens.onboarding.OnboardingNavAction
import com.mobdao.adoptapet.screens.petdetails.PetDetailsNavAction
import com.mobdao.adoptapet.screens.splash.SplashViewModel
import com.mobdao.common.kotlin.catchAndLogException
import com.mobdao.domain.models.AnimalType
import com.mobdao.domain.usecases.onboarding.HasCompletedOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val hasCompletedOnboardingUseCase: HasCompletedOnboardingUseCase,
) : ViewModel() {
    sealed interface NavAction {
        data object OnboardingScreen : NavAction

        data object SplashToHomeScreen : NavAction

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

    fun onNavAction(navAction: SplashViewModel.NavAction) {
        when (navAction) {
            SplashViewModel.NavAction.Completed -> {
                viewModelScope.launch {
                    val hasCompletedOnboarding =
                        hasCompletedOnboardingUseCase
                            .execute()
                            .catchAndLogException {
                                _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                            }.firstOrNull()
                            ?: return@launch
                    _navAction.value =
                        if (hasCompletedOnboarding) {
                            Event(SplashToHomeScreen)
                        } else {
                            Event(OnboardingScreen)
                        }
                }
            }
        }
    }

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
            FilterNavAction.FilterApplied -> {
                _navAction.value = Event(PreviousScreen)
            }
        }
    }

    fun onDismissGenericErrorDialog() {
        _uiState.update { it.copy(genericErrorDialogIsVisible = false) }
    }
}
