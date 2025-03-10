package com.mobdao.adoptapet.presentation.screens.petdetails

data class PetDetailsUiState(
    val petHeader: PetHeaderState = PetHeaderState(),
    val petCard: PetDetailsCardState = PetDetailsCardState(),
    val contact: ContactState = ContactState(),
    val genericErrorDialogIsVisible: Boolean = false,
) {
    data class PetHeaderState(
        val photoUrl: String = "",
        val name: String = "",
    )

    data class PetDetailsCardState(
        val breed: String = "",
        val age: String = "",
        val gender: String = "",
        val size: String = "",
        val distance: Float? = null,
        val description: String = "",
    )

    data class ContactState(
        val email: String = "",
        val phone: String = "",
    )
}
