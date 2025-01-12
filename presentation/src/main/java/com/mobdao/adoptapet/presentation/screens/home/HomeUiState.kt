package com.mobdao.adoptapet.presentation.screens.home

import com.mobdao.adoptapet.domain.models.AnimalType

data class HomeUiState(
    val progressIndicatorIsVisible: Boolean = false,
    val nextPageProgressIndicatorIsVisible: Boolean = false,
    val emptyListPlaceholderIsVisible: Boolean = false,
    val genericErrorDialogIsVisible: Boolean = false,
    val address: String = "",
) {
    data class PetState(
        val id: String,
        val type: AnimalType,
        val name: String,
        val breeds: BreedsState,
        val thumbnailUrl: String,
        val distance: Float? = null,
    )

    data class BreedsState(
        val primary: String?,
        val secondary: String?,
    )
}
