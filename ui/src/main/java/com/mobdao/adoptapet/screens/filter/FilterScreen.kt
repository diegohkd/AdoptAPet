package com.mobdao.adoptapet.screens.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobdao.adoptapet.screens.filter.FilterViewModel.NavAction.ApplyClicked
import com.mobdao.adoptapet.screens.filter.FilterViewModel.UiState

private val petTypes = listOf("Dog", "Cat", "Rabbit")

@Composable
fun FilterScreen(
    onApplyFilterRequested: () -> Unit,
    viewModel: FilterViewModel = hiltViewModel()
) {
    val navActionEvent by viewModel.navAction.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
        onPetTypeSelected = viewModel::onPetTypeSelected,
        onApplyClicked = viewModel::onApplyClicked,
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
    onPetTypeSelected: (String) -> Unit,
    onApplyClicked: () -> Unit,
) {
    var isTypeDropdownExpanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        LocationSearchBar(
            searchQuery = searchQuery,
            active = uiState.locationSearchModeIsActive,
            loading = uiState.locationProgressIndicatorIsVisible,
            autocompleteAddresses = uiState.locationAutocompleteAddresses,
            onSearchQueryChange = onSearchQueryChange,
            onLocationSearchActiveChange = onLocationSearchActiveChange,
            onAutocompleteAddressSelected = onAutocompleteAddressSelected,
        )
        Text(text = "Type")
        ExposedDropdownMenuBox(
            expanded = isTypeDropdownExpanded,
            onExpandedChange = {
                isTypeDropdownExpanded = it
            },
        ) {
            TextField(
                value = selectedOptionText,
                onValueChange = {
                    selectedOptionText = it
                },
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
                            selectedOptionText = it
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationSearchBar(
    searchQuery: String,
    active: Boolean,
    loading: Boolean,
    onLocationSearchActiveChange: (Boolean) -> Unit,
    autocompleteAddresses: List<String>,
    onSearchQueryChange: (String) -> Unit,
    onAutocompleteAddressSelected: (index: Int) -> Unit,
) {
    SearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = { /* no-op*/ },
        active = active,
        onActiveChange = onLocationSearchActiveChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(text = "Location") // TODO do not hardcode it
        }
    ) {
        LazyColumn {
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