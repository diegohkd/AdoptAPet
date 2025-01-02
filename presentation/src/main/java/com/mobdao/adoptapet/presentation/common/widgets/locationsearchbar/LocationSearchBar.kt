package com.mobdao.adoptapet.presentation.common.widgets.locationsearchbar

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mobdao.adoptapet.presentation.R
import com.mobdao.adoptapet.presentation.common.Event
import com.mobdao.adoptapet.presentation.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.presentation.common.widgets.locationsearchbar.LocationSearchBarUiAction.AutocompleteAddressSelected
import com.mobdao.adoptapet.presentation.common.widgets.locationsearchbar.LocationSearchBarUiAction.ClearSearchClicked
import com.mobdao.adoptapet.presentation.common.widgets.locationsearchbar.LocationSearchBarUiAction.CurrentLocationClicked
import com.mobdao.adoptapet.presentation.common.widgets.locationsearchbar.LocationSearchBarUiAction.Init
import com.mobdao.adoptapet.presentation.common.widgets.locationsearchbar.LocationSearchBarUiAction.LocationPermissionStateUpdated
import com.mobdao.adoptapet.presentation.common.widgets.locationsearchbar.LocationSearchBarUiAction.LocationSearchActiveChanged
import com.mobdao.adoptapet.presentation.common.widgets.locationsearchbar.LocationSearchBarUiAction.SearchQueryChanged
import com.mobdao.adoptapet.presentation.common.widgets.locationsearchbar.LocationSearchBarViewModel.SelectedAddressHolder
import com.mobdao.domain.models.Address

@Composable
fun LocationSearchBar(
    modifier: Modifier = Modifier,
    initialAddress: String = "",
    previewUiState: LocationSearchBarUiState = LocationSearchBarUiState(),
    previewSearchQuery: String = "",
    onAddressSelected: (Address?) -> Unit = {},
    onError: (Throwable?) -> Unit = {}, // TODO is it ok to pass throwable?
) {
    if (LocalInspectionMode.current) {
        UiContent(
            modifier = modifier,
            uiState = previewUiState,
            searchQuery = previewSearchQuery,
            onUiAction = {},
        )
    } else {
        LocationSearchBar(
            modifier = modifier,
            initialAddress = initialAddress,
            onAddressSelected = onAddressSelected,
            onError = onError,
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationSearchBar(
    modifier: Modifier = Modifier,
    initialAddress: String = "",
    onAddressSelected: (Address?) -> Unit,
    onError: (Throwable?) -> Unit,
    viewModel: LocationSearchBarViewModel = hiltViewModel(),
) {
    LaunchedEffect(initialAddress) {
        viewModel.onUiAction(Init(initialSearchQuery = initialAddress))
    }
    val uiState: LocationSearchBarUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val addressSelectedEvent: Event<SelectedAddressHolder?>? by viewModel.addressSelected.collectAsStateWithLifecycle()
    val errorEncounteredEvent: Event<Throwable?>? by viewModel.errorEncountered.collectAsStateWithLifecycle()

    addressSelectedEvent?.getContentIfNotHandled()?.let { event ->
        onAddressSelected(event.address)
    }
    errorEncounteredEvent?.getContentIfNotHandled()?.let(onError)

    val locationPermissionState =
        rememberMultiplePermissionsState(
            permissions =
                listOf(
                    ACCESS_COARSE_LOCATION,
                    ACCESS_FINE_LOCATION,
                ),
        )
    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        viewModel.onUiAction(
            LocationPermissionStateUpdated(areAllLocationPermissionsGranted = locationPermissionState.allPermissionsGranted),
        )
    }
    val askLocationPermissionEvent by viewModel.requestLocationPermission.collectAsStateWithLifecycle()
    askLocationPermissionEvent?.getContentIfNotHandled()?.let {
        locationPermissionState.launchMultiplePermissionRequest()
    }

    UiContent(
        modifier = modifier,
        uiState = uiState,
        searchQuery = viewModel.locationSearchQuery,
        onUiAction = viewModel::onUiAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UiContent(
    modifier: Modifier = Modifier,
    uiState: LocationSearchBarUiState,
    searchQuery: String,
    onUiAction: (LocationSearchBarUiAction) -> Unit,
) {
    DockedSearchBar(
        query = searchQuery,
        onQueryChange = { query -> onUiAction(SearchQueryChanged(query)) },
        onSearch = { /* no-op*/ },
        active = uiState.searchModeIsActive,
        onActiveChange = { isActive -> onUiAction(LocationSearchActiveChanged(isActive)) },
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(text = stringResource(R.string.location))
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onUiAction(ClearSearchClicked) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                    )
                }
            }
        },
    ) {
        LazyColumn {
            item {
                Text(
                    text = stringResource(R.string.current_location),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { onUiAction(CurrentLocationClicked) })
                            .padding(8.dp),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }
            if (uiState.progressIndicatorIsVisible) {
                item {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                itemsIndexed(uiState.locationAutocompleteAddresses) { index, address ->
                    Text(
                        text = address,
                        modifier =
                            Modifier
                                .padding(8.dp)
                                .clickable { onUiAction(AutocompleteAddressSelected(index)) },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun UiContentPreview() {
    AdoptAPetTheme {
        LocationSearchBar()
    }
}
