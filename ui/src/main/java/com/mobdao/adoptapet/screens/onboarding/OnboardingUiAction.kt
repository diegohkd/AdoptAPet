package com.mobdao.adoptapet.screens.onboarding

import com.mobdao.domain.models.Address

sealed interface OnboardingUiAction {
    data class AddressSelected(
        val address: Address?,
    ) : OnboardingUiAction

    data class FailedToGetAddress(
        val throwable: Throwable?,
    ) : OnboardingUiAction

    data object DismissGenericErrorDialog : OnboardingUiAction

    data object NextClicked : OnboardingUiAction
}
