package com.mobdao.adoptapet.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.screens.onboarding.OnboardingViewModel.NavAction.Completed
import com.mobdao.common.kotlin.catchAndLogException
import com.mobdao.domain.models.Address
import com.mobdao.domain.usecases.onboarding.CompleteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

// TODO disable next button if location is not selected

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completedOnboardingUseCase: CompleteOnboardingUseCase,
) : ViewModel() {

    sealed interface NavAction {
        data object Completed : NavAction
    }

    data class UiState(
        val selectedAddress: String = "",
        val genericErrorDialogIsVisible: Boolean = false,
    )

    private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
    val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var address: Address? = null

    fun onFailedToGetAddress(throwable: Throwable?) {
        throwable?.let(Timber::e)
        _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
    }

    fun onAddressSelected(address: Address) {
        this.address = address
        _uiState.update { it.copy(selectedAddress = address.addressLine) }
    }

    fun onNextClicked() {
        val address = address

        if (address == null) {
            _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
            return
        }
        viewModelScope.launch {
            completedOnboardingUseCase.execute(address)
                .catchAndLogException {
                    _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                }
                .collect {
                    _navAction.value = Event(Completed)
                }
        }
    }

    fun onDismissGenericErrorDialog() {
        _uiState.update { it.copy(genericErrorDialogIsVisible = false) }
    }
}