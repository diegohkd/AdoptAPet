package com.mobdao.adoptapet.screens.filter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobdao.adoptapet.R
import com.mobdao.adoptapet.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.common.widgets.locationsearchbar.LocationSearchBar
import com.mobdao.adoptapet.screens.filter.FilterViewModel.NavAction
import com.mobdao.adoptapet.screens.filter.FilterViewModel.UiState
import com.mobdao.adoptapet.screens.filter.FilterViewModel.UiState.PetType
import com.mobdao.adoptapet.screens.filter.FilterViewModel.UiState.PetTypes
import com.mobdao.domain.models.Address

private val petTypes =
    listOf(
        "Dog",
        "Cat",
        "Rabbit",
        "Bird",
        "Small & Furry",
        "Horse",
        "Barnyard",
        "Scales, Fins & Other",
    )

@Composable
fun FilterScreen(
    onNavAction: (NavAction) -> Unit,
    viewModel: FilterViewModel = hiltViewModel(),
) {
    val navActionEvent by viewModel.navAction.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    navActionEvent?.getContentIfNotHandled()?.let(onNavAction)

    UiContent(
        uiState = uiState,
        onFailedToGetAddress = viewModel::onFailedToSearchAddress,
        onAddressSelected = viewModel::onSearchedAddressSelected,
        onPetTypeSelected = viewModel::onPetTypeSelected,
        onPetTypeClicked = viewModel::onPetTypeClicked,
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
    onPetTypeClicked: (PetType) -> Unit,
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
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { internalPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(internalPadding),
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
                        },
                    ) {
                        petTypes.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    isTypeDropdownExpanded = false
                                    onPetTypeSelected(it)
                                },
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
                PetType(
                    petTypes = uiState.petTypes,
                    onPetTypeClicked = onPetTypeClicked,
                )
            }
        }
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = onDismissGenericErrorDialog)
    }
}

@Composable
private fun PetType(
    petTypes: PetTypes,
    onPetTypeClicked: (PetType) -> Unit,
) {
    LazyVerticalGrid(
        // TODO set minSize based on screen width?
        columns = GridCells.Adaptive(minSize = 100.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // TODO set key provider?
        items(petTypes.types) { petType ->
            Card(
                onClick = { onPetTypeClicked(petType) },
                // TODO improve this
                border = BorderStroke(2.dp, if (petType.isSelected) Color.Black else Color.White),
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = petTypes.longestTypeNamePlaceholder, color = Color(0x00000000))
                    Text(text = petType.name, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
@Preview
private fun PetTypePreview() {
    AdoptAPetTheme {
        PetType(
            PetTypes(
                listOf(
                    PetType(
                        name = "Dog",
                        isSelected = false,
                    ),
                    PetType(
                        name = "Cat",
                        isSelected = false,
                    ),
                    PetType(
                        name = "Rabbit",
                        isSelected = false,
                    ),
                    PetType(
                        name = "Bird",
                        isSelected = false,
                    ),
                    PetType(
                        name = "Small & Furry",
                        isSelected = false,
                    ),
                    PetType(
                        name = "Horse",
                        isSelected = false,
                    ),
                    PetType(
                        name = "Barnyard",
                        isSelected = false,
                    ),
                    PetType(
                        name = "Scales, Fins & Other",
                        isSelected = false,
                    ),
                ),
                longestTypeNamePlaceholder = "Scales, Fins & Other",
            ),
            {},
        )
    }
}
