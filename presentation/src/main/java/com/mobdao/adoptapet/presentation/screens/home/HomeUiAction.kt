package com.mobdao.adoptapet.presentation.screens.home

import com.mobdao.adoptapet.presentation.screens.home.HomeUiState.PetState

sealed interface HomeUiAction {
    data class PetClicked(
        val pet: PetState,
    ) : HomeUiAction

    data object DismissGenericErrorDialog : HomeUiAction

    data object FilterClicked : HomeUiAction
}
