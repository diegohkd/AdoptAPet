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

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completedOnboardingUseCase: CompleteOnboardingUseCase,
) : ViewModel() {

    sealed interface NavAction {
        data object Completed : NavAction
    }

    data class UiState(
        val selectedAddress: String = "",
        val nextButtonIsEnabled: Boolean = false,
        val genericErrorDialogIsVisible: Boolean = false,
    )

    private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
    val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val address = MutableStateFlow<Address?>(null)

    init {
        handleNextButtonEnabledState()
    }

    fun onFailedToGetAddress(throwable: Throwable?) {
        throwable?.let(Timber::e)
        _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
    }

    fun onAddressSelected(address: Address?) {
        this.address.value = address
    }

    fun onNextClicked() {
        viewModelScope.launch {
            completedOnboardingUseCase.execute(address.value!!)
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