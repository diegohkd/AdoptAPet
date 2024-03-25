package com.mobdao.adoptapet.screens.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobdao.adoptapet.R
import com.mobdao.adoptapet.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.common.widgets.locationsearchbar.LocationSearchBar
import com.mobdao.adoptapet.screens.filter.FilterViewModel.NavAction.FilterApplied
import com.mobdao.adoptapet.screens.filter.FilterViewModel.UiState
import com.mobdao.domain.models.Address

private val petTypes = listOf("Dog", "Cat", "Rabbit")

@Composable
fun FilterScreen(
    onFilterApplied: () -> Unit,
    viewModel: FilterViewModel = hiltViewModel()
) {
    val navActionEvent by viewModel.navAction.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (navActionEvent?.getContentIfNotHandled()) {
        is FilterApplied -> onFilterApplied()
        null -> {}
    }

    UiContent(
        uiState = uiState,
        onFailedToGetAddress = viewModel::onFailedToGetAddress,
        onAddressSelected = viewModel::onAddressSelected,
        onPetTypeSelected = viewModel::onPetTypeSelected,
        onApplyClicked = viewModel::onApplyClicked,
        onDismissGenericErrorDialog = viewModel::onDismissGenericErrorDialog,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UiContent(
    uiState: UiState,
    onFailedToGetAddress: (Throwable?) -> Unit,
    onAddressSelected: (Address) -> Unit,
    onPetTypeSelected: (String) -> Unit,
    onApplyClicked: () -> Unit,
    onDismissGenericErrorDialog: () -> Unit,
) {
    var isTypeDropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        LocationSearchBar(
            selectedAddress = uiState.selectedAddress,
            onAddressSelected = onAddressSelected,
            onError = onFailedToGetAddress,
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
            Text(text = stringResource(R.string.apply))
        }
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = onDismissGenericErrorDialog)
    }
}