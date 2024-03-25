package com.mobdao.adoptapet.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.navigation.NavigationViewModel.NavAction.*
import com.mobdao.common.kotlin.catchAndLogException
import com.mobdao.domain.usecases.onboarding.HasCompletedOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
        data class PetDetailsScreen(val petId: String) : NavAction
        data object PreviousScreen : NavAction
    }

    data class UiState(val genericErrorDialogIsVisible: Boolean = false)

    private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
    val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onSplashCompleted() {
        viewModelScope.launch {
            val hasCompletedOnboarding = hasCompletedOnboardingUseCase.execute()
                .catchAndLogException {
                    _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                }
                .firstOrNull()
                ?: false
            _navAction.value = if (hasCompletedOnboarding) {
                Event(SplashToHomeScreen)
            } else {
                Event(OnboardingScreen)
            }
        }
    }

    fun onOnboardingCompleted() {
        _navAction.value = Event(OnboardingToHomeScreen)
    }

    fun onPetClicked(petId: String) {
        _navAction.value = Event(PetDetailsScreen(petId))
    }

    fun onFilterClicked() {
        _navAction.value = Event(FilterScreen)
    }

    fun onFilterApplied() {
        _navAction.value = Event(PreviousScreen)
    }

    fun onDismissGenericErrorDialog() {
        _uiState.update { it.copy(genericErrorDialogIsVisible = false) }
    }
}