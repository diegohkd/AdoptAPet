package com.mobdao.adoptapet.common.widgets.locationsearchbar

import com.mobdao.domain.models.Address

data class LocationSearchBarUiState(
    val selectedAddress: Address? = null,
    val searchModeIsActive: Boolean = false,
    val progressIndicatorIsVisible: Boolean = false,
    val locationAutocompleteAddresses: List<String> = emptyList(),
)
