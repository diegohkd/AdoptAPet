package com.mobdao.adoptapet.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.kotlin.catchAndLogException
import com.mobdao.adoptapet.domain.models.Address
import com.mobdao.adoptapet.domain.usecases.onboarding.CompleteOnboardingUseCase
import com.mobdao.adoptapet.presentation.common.Event
import com.mobdao.adoptapet.presentation.screens.onboarding.OnboardingNavAction.Completed
import com.mobdao.adoptapet.presentation.screens.onboarding.OnboardingUiAction.AddressSelected
import com.mobdao.adoptapet.presentation.screens.onboarding.OnboardingUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.screens.onboarding.OnboardingUiAction.FailedToGetAddress
import com.mobdao.adoptapet.presentation.screens.onboarding.OnboardingUiAction.NextClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completedOnboardingUseCase: CompleteOnboardingUseCase,
) : ViewModel() {
    private val _navAction = MutableStateFlow<Event<OnboardingNavAction>?>(null)
    val navAction: StateFlow<Event<OnboardingNavAction>?> = _navAction.asStateFlow()

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val address = MutableStateFlow<Address?>(null)

    init {
        handleNextButtonEnabledState()
    }

    fun onUiAction(action: OnboardingUiAction) {
        when (action) {
            is AddressSelected -> onAddressSelected(address = action.address)
            is FailedToGetAddress -> onFailedToGetAddress(action.throwable)
            DismissGenericErrorDialog -> onDismissGenericErrorDialog()
            NextClicked -> onNextClicked()
        }
    }

    private fun onAddressSelected(address: Address?) {
        this.address.value = address
    }

    private fun onDismissGenericErrorDialog() {
        _uiState.update { it.copy(genericErrorDialogIsVisible = false) }
    }

    private fun onFailedToGetAddress(throwable: Throwable?) {
        throwable?.let(Timber::e)
        _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
    }

    private fun onNextClicked() {
        viewModelScope.launch {
            completedOnboardingUseCase
                .execute(address.value!!)
                .catchAndLogException {
                    _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                }.collect {
                    _navAction.value = Event(Completed)
                }
        }
    }

    private fun handleNextButtonEnabledState() {
        viewModelScope.launch {
            address
                .collect { address ->
                    _uiState.update {
                        it.copy(nextButtonIsEnabled = address != null)
                    }
                }
        }
    }
}
