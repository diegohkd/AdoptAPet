package com.mobdao.adoptapet.presentation.screens.home

import com.mobdao.domain.models.AnimalType

sealed interface HomeNavAction {
    data class PetClicked(
        val petId: String,
        val type: AnimalType,
    ) : HomeNavAction

    data object FilterClicked : HomeNavAction
}
