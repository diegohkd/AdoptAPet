package com.mobdao.adoptapet.common.widgets.locationsearchbar

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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mobdao.adoptapet.R
import com.mobdao.domain.models.Address

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationSearchBar(
    modifier: Modifier = Modifier,
    initialAddress: String = "",
    paddingHorizontal: Dp = 0.dp,
    onAddressSelected: (Address?) -> Unit,
    onError: (Throwable?) -> Unit, // TODO is it ok to pass throwable?
    viewModel: LocationSearchBarViewModel = hiltViewModel(),
) {
    LaunchedEffect(initialAddress) {
        viewModel.init(initialAddress)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val addressSelectedEvent by viewModel.addressSelected.collectAsStateWithLifecycle()
    val errorEncounteredEvent by viewModel.errorEncountered.collectAsStateWithLifecycle()

    addressSelectedEvent?.getContentIfNotHandled()?.let {
        onAddressSelected(it.address)
    }
    errorEncounteredEvent?.getContentIfNotHandled()?.let(onError)

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION
        )
    )
    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        viewModel.onLocationPermissionStateUpdated(
            areAllLocationPermissionsGranted = locationPermissionState.allPermissionsGranted,
        )
    }
    val askLocationPermissionEvent by viewModel.requestLocationPermission.collectAsStateWithLifecycle()
    askLocationPermissionEvent?.getContentIfNotHandled()?.let {
        locationPermissionState.launchMultiplePermissionRequest()
    }

    UiContent(
        paddingHorizontal = paddingHorizontal,
        modifier = modifier,
        searchQuery = viewModel.locationSearchQuery,
        active = uiState.searchModeIsActive,
        loading = uiState.progressIndicatorIsVisible,
        autocompleteAddresses = uiState.locationAutocompleteAddresses,
        onSearchQueryChange = viewModel::onLocationSearchQueryChanged,
        onLocationSearchActiveChange = viewModel::onSearchModeActiveChange,
        onAutocompleteAddressSelected = viewModel::onAddressItemClicked,
        onCurrentLocationClicked = viewModel::onCurrentLocationClicked,
        onClearClicked = viewModel::onClearLocationSearchClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UiContent(
    paddingHorizontal: Dp,
    modifier: Modifier = Modifier,
    searchQuery: String,
    active: Boolean,
    loading: Boolean,
    onLocationSearchActiveChange: (Boolean) -> Unit,
    autocompleteAddresses: List<String>,
    onSearchQueryChange: (String) -> Unit,
    onAutocompleteAddressSelected: (index: Int) -> Unit,
    onCurrentLocationClicked: () -> Unit,
    onClearClicked: () -> Unit,
) {
    SearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = { /* no-op*/ },
        active = active,
        onActiveChange = onLocationSearchActiveChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = if (active) 0.dp else paddingHorizontal),
        placeholder = {
            Text(text = stringResource(R.string.location))
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = onClearClicked) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = ""
                    )
                }
            }
        },
    ) {
        LazyColumn {
            item {
                Text(
                    text = "Current location",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable(onClick = onCurrentLocationClicked),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }
            if (loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                itemsIndexed(autocompleteAddresses) { index, address ->
                    Text(
                        text = address,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onAutocompleteAddressSelected(index) },
                    )
                }
            }
        }
    }
}