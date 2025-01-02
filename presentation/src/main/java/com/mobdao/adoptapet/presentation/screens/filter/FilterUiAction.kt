package com.mobdao.adoptapet.presentation.screens.filter

import com.mobdao.adoptapet.domain.models.Address
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeState

sealed interface FilterUiAction {
    data class AddressSelected(
        val address: Address?,
    ) : FilterUiAction

    data class FailedToGetAddress(
        val throwable: Throwable?,
    ) : FilterUiAction

    data class PetTypeClicked(
        val petTypeState: PetTypeState,
    ) : FilterUiAction

    data class PetTypeSelected(
        val petType: String,
    ) : FilterUiAction

    data object ApplyClicked : FilterUiAction

    data object DismissGenericErrorDialog : FilterUiAction
}
