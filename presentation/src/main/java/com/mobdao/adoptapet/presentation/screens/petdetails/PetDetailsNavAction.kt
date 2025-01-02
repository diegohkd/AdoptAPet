package com.mobdao.adoptapet.presentation.screens.petdetails

sealed interface PetDetailsNavAction {
    data object BackButtonClicked : PetDetailsNavAction
}
