package com.mobdao.adoptapet.presentation.screens.petdetails

sealed interface PetDetailsUiAction {
    data object BackButtonClicked : PetDetailsUiAction

    data object DismissGenericErrorDialog : PetDetailsUiAction
}
