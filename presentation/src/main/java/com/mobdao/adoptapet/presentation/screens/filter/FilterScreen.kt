package com.mobdao.adoptapet.presentation.screens.filter

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobdao.adoptapet.presentation.R
import com.mobdao.adoptapet.presentation.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.presentation.common.widgets.BackButton
import com.mobdao.adoptapet.presentation.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.presentation.common.widgets.locationsearchbar.LocationSearchBar
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.AddressSelected
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.ApplyClicked
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.BackClicked
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.FailedToGetAddress
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.PetGenderClicked
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.PetTypeClicked
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.FilterProperty
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.FilterProperty.PetGenderState
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.FilterProperty.PetTypeState
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetGenderNameState
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeNameState

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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.filter)) },
                navigationIcon = {
                    BackButton(onClicked = { onUiAction(BackClicked) })
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
                FilterPropertyGrid(
                    filterTitle = stringResource(R.string.pet_type),
                    properties = uiState.petTypes,
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    modifier = Modifier.padding(top = 8.dp),
                    onPropertyClicked = { petTypeState ->
                        onUiAction(PetTypeClicked(petTypeFilter = petTypeState.type))
                    },
                )
                FilterPropertyGrid(
                    filterTitle = stringResource(R.string.gender),
                    properties = uiState.petGenders,
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(top = 8.dp),
                    onPropertyClicked = { petGenderState ->
                        onUiAction(PetGenderClicked(gender = petGenderState.gender))
                    },
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { onUiAction(ApplyClicked) },
                    modifier =
                        Modifier
                            .padding(bottom = 24.dp)
                            .fillMaxWidth(),
                    enabled = uiState.isApplyButtonEnabled,
                ) {
                    Text(text = stringResource(R.string.apply_filter))
                }
            }
        }
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = { onUiAction(DismissGenericErrorDialog) })
    }
}

@Composable
private fun <T : FilterProperty> FilterPropertyGrid(
    filterTitle: String,
    properties: List<T>,
    columns: GridCells,
    modifier: Modifier,
    onPropertyClicked: (T) -> Unit,
) {
    Text(
        text = filterTitle,
        modifier = Modifier.padding(top = 8.dp),
        fontSize = 18.sp,
        fontWeight = Bold,
    )
    val longestName: String = rememberLongestName(properties)
    LazyVerticalGrid(
        columns = columns,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        items(properties) { property ->
            Card(
                onClick = { onPropertyClicked(property) },
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    ),
                border =
                    BorderStroke(
                        width = 2.dp,
                        color =
                            if (property.isSelected) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHighest
                            },
                    ),
            ) {
                Box(
                    modifier =
                        Modifier
                            .padding(8.dp)
                            .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = longestName,
                        color = Transparent,
                    )
                    Text(
                        text = stringResource(property.nameRes),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberLongestName(properties: List<FilterProperty>): String {
    val context = LocalContext.current
    return remember {
        properties
            .map {
                context.getString(it.nameRes)
            }.maxBy { petTypeName ->
                petTypeName.length
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
                    PetTypeNameState.entries.map { petType ->
                        PetTypeState(
                            type = petType,
                            isSelected = true,
                        )
                    },
                petGenders =
                    PetGenderNameState.entries.map { petGender ->
                        PetGenderState(
                            gender = petGender,
                            isSelected = false,
                        )
                    },
            ),
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PetTypeNightModelPreview() {
    AdoptAPetTheme {
        UiContent(
            FilterUiState(
                petTypes =
                    PetTypeNameState.entries.map { petType ->
                        PetTypeState(
                            type = petType,
                            isSelected = true,
                        )
                    },
                petGenders =
                    PetGenderNameState.entries.map { petGender ->
                        PetGenderState(
                            gender = petGender,
                            isSelected = false,
                        )
                    },
            ),
        )
    }
}
