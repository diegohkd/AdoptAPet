package com.mobdao.adoptapet.common.widgets.locationsearchbar

sealed interface LocationSearchBarUiAction {
    data class AutocompleteAddressSelected(
        val index: Int,
    ) : LocationSearchBarUiAction

    data class Init(
        val initialSearchQuery: String,
    ) : LocationSearchBarUiAction

    data class LocationPermissionStateUpdated(
        val areAllLocationPermissionsGranted: Boolean,
    ) : LocationSearchBarUiAction

    data class LocationSearchActiveChanged(
        val isActive: Boolean,
    ) : LocationSearchBarUiAction

    data class SearchQueryChanged(
        val newQuery: String,
    ) : LocationSearchBarUiAction

    data object ClearSearchClicked : LocationSearchBarUiAction

    data object CurrentLocationClicked : LocationSearchBarUiAction
}
