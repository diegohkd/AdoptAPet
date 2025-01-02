package com.mobdao.adoptapet.presentation.screens.onboarding

sealed interface OnboardingNavAction {
    data object Completed : OnboardingNavAction
}
