package com.mobdao.adoptapet.screens.onboarding

sealed interface OnboardingNavAction {
    data object Completed : OnboardingNavAction
}
