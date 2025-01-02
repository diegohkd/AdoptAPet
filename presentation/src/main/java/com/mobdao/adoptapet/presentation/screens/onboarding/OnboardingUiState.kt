package com.mobdao.adoptapet.presentation.screens.onboarding

data class OnboardingUiState(
    val selectedAddress: String = "",
    val nextButtonIsEnabled: Boolean = false,
    val genericErrorDialogIsVisible: Boolean = false,
)
