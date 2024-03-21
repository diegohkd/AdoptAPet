package com.mobdao.adoptapet.screens.filter

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mobdao.adoptapet.R
import com.mobdao.adoptapet.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.screens.filter.FilterViewModel.NavAction.ApplyClicked
import com.mobdao.adoptapet.screens.filter.FilterViewModel.UiState

private val petTypes = listOf("Dog", "Cat", "Rabbit")

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FilterScreen(
    onApplyFilterRequested: () -> Unit,
    viewModel: FilterViewModel = hiltViewModel()
) {
    val navActionEvent by viewModel.navAction.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
    )
    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        viewModel.onLocationPermissionStateUpdated(
            areAllLocationPermissionsGranted = locationPermissionState.allPermissionsGranted,
        )
    }

    when (navActionEvent?.getContentIfNotHandled()) {
        is ApplyClicked -> onApplyFilterRequested()
        null -> {}
    }

    FilterContent(
        searchQuery = viewModel.locationSearchQuery,
        uiState = uiState,
        onSearchQueryChange = viewModel::onLocationSearchQueryChanged,
        onLocationSearchActiveChange = viewModel::onLocationSearchActiveChange,
        onAutocompleteAddressSelected = viewModel::onAddressSelected,
        onCurrentLocationClicked = viewModel::onCurrentLocationClicked,
        onClearLocationSearchClicked = viewModel::onClearLocationSearchClicked,
        onPetTypeSelected = viewModel::onPetTypeSelected,
        onApplyClicked = viewModel::onApplyClicked,
        onDismissGenericErrorDialog = viewModel::onDismissGenericErrorDialog,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterContent(
    searchQuery: String,
    uiState: UiState,
    onSearchQueryChange: (String) -> Unit,
    onLocationSearchActiveChange: (Boolean) -> Unit,
    onAutocompleteAddressSelected: (index: Int) -> Unit,
    onCurrentLocationClicked: () -> Unit,
    onClearLocationSearchClicked: () -> Unit,
    onPetTypeSelected: (String) -> Unit,
    onApplyClicked: () -> Unit,
    onDismissGenericErrorDialog: () -> Unit,
) {
    var isTypeDropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        LocationSearchBar(
            searchQuery = searchQuery,
            active = uiState.locationSearchModeIsActive,
            loading = uiState.locationProgressIndicatorIsVisible,
            autocompleteAddresses = uiState.locationAutocompleteAddresses,
            showCurrentLocationItem = uiState.isSelectingCurrentLocationEnabled,
            onSearchQueryChange = onSearchQueryChange,
            onLocationSearchActiveChange = onLocationSearchActiveChange,
            onAutocompleteAddressSelected = onAutocompleteAddressSelected,
            onCurrentLocationClicked = onCurrentLocationClicked,
            onClearClicked = onClearLocationSearchClicked,
        )
        Text(text = "Type")
        ExposedDropdownMenuBox(
            expanded = isTypeDropdownExpanded,
            onExpandedChange = {
                isTypeDropdownExpanded = it
            },
        ) {
            TextField(
                value = uiState.petType,
                onValueChange = { /*no-op*/ },
                modifier = Modifier.menuAnchor(),
                readOnly = true,
            )
            ExposedDropdownMenu(
                expanded = isTypeDropdownExpanded,
                onDismissRequest = {
                    isTypeDropdownExpanded = false
                }
            ) {
                petTypes.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            isTypeDropdownExpanded = false
                            onPetTypeSelected(it)
                        }
                    )
                }
            }
        }
        Button(
            onClick = onApplyClicked,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Apply")
        }
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = onDismissGenericErrorDialog)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationSearchBar(
    searchQuery: String,
    active: Boolean,
    loading: Boolean,
    showCurrentLocationItem: Boolean,
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
        modifier = Modifier.fillMaxWidth(),
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
            if (showCurrentLocationItem) {
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
            }
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