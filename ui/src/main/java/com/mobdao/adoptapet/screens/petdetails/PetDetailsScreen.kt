package com.mobdao.adoptapet.screens.petdetails

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.mobdao.adoptapet.common.layouts.PetSurface
import com.mobdao.adoptapet.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.common.theme.color.ColorSchema
import com.mobdao.adoptapet.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.screens.petdetails.PetDetailsViewModel.UiState
import com.mobdao.adoptapet.utils.extensions.toColorSchema
import com.mobdao.domain.models.AnimalType

@Composable
fun PetDetailsScreen(
    animalType: AnimalType,
    viewModel: PetDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UiContent(
        animalType = animalType,
        uiState = uiState,
        onDismissGenericErrorDialog = viewModel::onDismissGenericErrorDialog,
    )
}

@Composable
private fun UiContent(
    animalType: AnimalType,
    uiState: UiState,
    onDismissGenericErrorDialog: () -> Unit = {},
) {
    val colorSchema: ColorSchema = remember(animalType) { animalType.toColorSchema() }
    AdoptAPetTheme(colorSchema) {
        PetSurface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding(),
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
                    contentScale = ContentScale.Crop
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
    }
}
