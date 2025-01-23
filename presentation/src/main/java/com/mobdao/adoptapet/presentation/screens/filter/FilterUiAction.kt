package com.mobdao.adoptapet.presentation.screens.filter

import com.mobdao.adoptapet.domain.models.Address
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetGenderNameState
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeNameState

sealed interface FilterUiAction {
    data class AddressSelected(
        val address: Address?,
    ) : FilterUiAction

    data class FailedToGetAddress(
        val throwable: Throwable?,
    ) : FilterUiAction

    data class PetTypeClicked(
        val petTypeFilter: PetTypeNameState,
    ) : FilterUiAction

    data class PetGenderClicked(
        val gender: PetGenderNameState,
    ) : FilterUiAction

    data object ApplyClicked : FilterUiAction

    data object BackClicked : FilterUiAction

    data object DismissGenericErrorDialog : FilterUiAction
}
