package com.mobdao.adoptapet.screens.pet_details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.mobdao.adoptapet.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.screens.pet_details.PetDetailsViewModel.UiState

@Composable
fun PetDetailsScreen(viewModel: PetDetailsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PetDetailsContent(
        uiState = uiState,
        onDismissGenericErrorDialog = viewModel::onDismissGenericErrorDialog,
    )
}

@Composable
private fun PetDetailsContent(
    uiState: UiState,
    onDismissGenericErrorDialog: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = uiState.photoUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            onState = {
                if (it is AsyncImagePainter.State.Error) {
                    it.result.throwable.printStackTrace()
                }
            },
        )
        Text(
            text = uiState.petName,
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 32.sp
        )
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = onDismissGenericErrorDialog)
    }
}