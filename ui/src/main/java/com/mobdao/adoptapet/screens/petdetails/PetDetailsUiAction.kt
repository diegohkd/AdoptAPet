package com.mobdao.adoptapet.screens.petdetails

// onBackButtonClicked: () -> Unit = {},
// onDismissGenericErrorDialog: () -> Unit = {},

sealed interface PetDetailsUiAction {
    data object BackButtonClicked : PetDetailsUiAction

    data object DismissGenericErrorDialog : PetDetailsUiAction
}
