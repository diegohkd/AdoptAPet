package com.mobdao.adoptapet.screens.filter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobdao.adoptapet.R
import com.mobdao.adoptapet.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.common.widgets.locationsearchbar.LocationSearchBar
import com.mobdao.adoptapet.screens.filter.FilterViewModel.NavAction
import com.mobdao.adoptapet.screens.filter.FilterViewModel.UiState
import com.mobdao.domain.models.Address

private val petTypes = listOf(
    "Dog",
    "Cat",
    "Rabbit",
    "Bird",
    "Small & Furry",
    "Horse",
    "Barnyard",
    "Scales, Fins & Other"
)

@Composable
fun FilterScreen(
    onNavAction: (NavAction) -> Unit,
    viewModel: FilterViewModel = hiltViewModel()
) {
    val navActionEvent by viewModel.navAction.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    navActionEvent?.getContentIfNotHandled()?.let(onNavAction)

    UiContent(
        uiState = uiState,
        onFailedToGetAddress = viewModel::onFailedToSearchAddress,
        onAddressSelected = viewModel::onSearchedAddressSelected,
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
    onAddressSelected: (Address?) -> Unit,
    onPetTypeSelected: (String) -> Unit,
    onApplyClicked: () -> Unit,
    onDismissGenericErrorDialog: () -> Unit,
) {
    var isTypeDropdownExpanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Filter") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) { internalPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(internalPadding)
        ) {
            LocationSearchBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                initialAddress = uiState.initialAddress,
                onAddressSelected = onAddressSelected,
                onError = onFailedToGetAddress,
            )
            Column(modifier = Modifier.padding(top = 72.dp, start = 16.dp, end = 16.dp)) {
                Text(text = stringResource(R.string.type))
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
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enabled = uiState.isApplyButtonEnabled,
                ) {
                    Text(text = stringResource(R.string.apply))
                }
            }
        }
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = onDismissGenericErrorDialog)
    }
}
