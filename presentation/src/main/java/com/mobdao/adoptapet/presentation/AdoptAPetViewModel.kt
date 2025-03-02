package com.mobdao.adoptapet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.kotlin.catchAndLogException
import com.mobdao.adoptapet.domain.usecases.onboarding.HasCompletedOnboardingUseCase
import com.mobdao.adoptapet.presentation.AdoptAPetUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.navigation.Destination.Home
import com.mobdao.adoptapet.presentation.navigation.Destination.Onboarding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MINIMUM_SPLASH_LOADING_TIME = 1000L

@HiltViewModel
class AdoptAPetViewModel @Inject constructor(
    private val hasCompletedOnboardingUseCase: HasCompletedOnboardingUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdoptAPetUiState())
    val uiState: StateFlow<AdoptAPetUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // TODO replace fake loading with actual loading
            delay(MINIMUM_SPLASH_LOADING_TIME)
            val hasCompletedOnboarding =
                hasCompletedOnboardingUseCase
                    .execute()
                    .catchAndLogException {
                        _uiState.update { it.copy(isGenericErrorDialogVisible = true) }
                    }.firstOrNull()
                    ?: return@launch
            _uiState.update {
                it.copy(
                    isSplashVisible = false,
                    startDestination =
                        if (hasCompletedOnboarding) {
                            Home
                        } else {
                            Onboarding
                        },
                )
            }
        }
    }

    fun onUiAction(action: AdoptAPetUiAction) {
        when (action) {
            DismissGenericErrorDialog -> {
                _uiState.update { it.copy(isGenericErrorDialogVisible = false) }
            }
        }
    }
}
