package com.mobdao.adoptapet.screens.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobdao.adoptapet.screens.filter.FilterViewModel.NavAction.ApplyClicked

private val petTypes = listOf("Dog", "Cat", "Rabbit")

@Composable
fun FilterScreen(
    onApplyFilterRequested: () -> Unit,
    viewModel: FilterViewModel = hiltViewModel()
) {
    val navActionEvent by viewModel.navAction.collectAsStateWithLifecycle()

    when (navActionEvent?.getContentIfNotHandled()) {
        is ApplyClicked -> onApplyFilterRequested()
        null -> {}
    }

    FilterContent(
        onPetTypeSelected = viewModel::onPetTypeSelected,
        onApplyClicked = viewModel::onApplyClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterContent(
    onPetTypeSelected: (String) -> Unit,
    onApplyClicked: () -> Unit,
) {
    var isTypeDropdownExpanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
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