package com.mobdao.adoptapet.screens.filter

data class FilterUiState(
    val initialAddress: String = "",
    val petType: String = "",
    val petTypes: PetTypesState = PetTypesState(),
    val isApplyButtonEnabled: Boolean = false,
    val genericErrorDialogIsVisible: Boolean = false,
) {
    data class PetTypesState(
        val types: List<PetTypeState> = emptyList(),
        val longestTypeNamePlaceholder: String = "",
    )

    data class PetTypeState(
        val name: String,
        val isSelected: Boolean,
    )
}
