package com.mobdao.adoptapet.screens.petdetails

sealed interface PetDetailsNavAction {
    data object BackButtonClicked : PetDetailsNavAction
}
