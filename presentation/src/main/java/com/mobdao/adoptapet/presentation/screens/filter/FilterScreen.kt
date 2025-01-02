package com.mobdao.adoptapet.presentation.screens.filter

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
import com.mobdao.adoptapet.presentation.R
import com.mobdao.adoptapet.presentation.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.presentation.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.presentation.common.widgets.locationsearchbar.LocationSearchBar
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.AddressSelected
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.ApplyClicked
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.FailedToGetAddress
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.PetTypeClicked
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.PetTypeSelected
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeState
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypesState

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
    onNavAction: (FilterNavAction) -> Unit,
    viewModel: FilterViewModel = hiltViewModel(),
) {
    val navActionEvent by viewModel.navAction.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    navActionEvent?.getContentIfNotHandled()?.let(onNavAction)

    UiContent(
        uiState = uiState,
        onUiAction = viewModel::onUiAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UiContent(
    uiState: FilterUiState,
    onUiAction: (FilterUiAction) -> Unit = {},
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
                onAddressSelected = { onUiAction(AddressSelected(it)) },
                onError = { error -> onUiAction(FailedToGetAddress(error)) },
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
                        petTypes.forEach { petType ->
                            DropdownMenuItem(
                                text = { Text(petType) },
                                onClick = {
                                    isTypeDropdownExpanded = false
                                    onUiAction(PetTypeSelected(petType))
                                },
                            )
                        }
                    }
                }
                Button(
                    onClick = { onUiAction(ApplyClicked) },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enabled = uiState.isApplyButtonEnabled,
                ) {
                    Text(text = stringResource(R.string.apply))
                }
                PetType(
                    petTypes = uiState.petTypes,
                    onPetTypeClicked = { onUiAction(PetTypeClicked(it)) },
                )
            }
        }
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = { onUiAction(DismissGenericErrorDialog) })
    }
}

@Composable
private fun PetType(
    petTypes: PetTypesState,
    onPetTypeClicked: (PetTypeState) -> Unit,
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
        UiContent(
            FilterUiState(
                petTypes =
                    PetTypesState(
                        listOf(
                            PetTypeState(
                                name = "Dog",
                                isSelected = false,
                            ),
                            PetTypeState(
                                name = "Cat",
                                isSelected = false,
                            ),
                            PetTypeState(
                                name = "Rabbit",
                                isSelected = false,
                            ),
                            PetTypeState(
                                name = "Bird",
                                isSelected = false,
                            ),
                            PetTypeState(
                                name = "Small & Furry",
                                isSelected = false,
                            ),
                            PetTypeState(
                                name = "Horse",
                                isSelected = false,
                            ),
                            PetTypeState(
                                name = "Barnyard",
                                isSelected = false,
                            ),
                            PetTypeState(
                                name = "Scales, Fins & Other",
                                isSelected = false,
                            ),
                        ),
                        longestTypeNamePlaceholder = "Scales, Fins & Other",
                    ),
            ),
        )
    }
}
