package com.mobdao.adoptapet.screens.home

import com.mobdao.adoptapet.screens.home.HomeUiState.PetState

sealed interface HomeUiAction {
    data class PetClicked(
        val pet: PetState,
    ) : HomeUiAction

    data object DismissGenericErrorDialog : HomeUiAction

    data object FilterClicked : HomeUiAction
}
