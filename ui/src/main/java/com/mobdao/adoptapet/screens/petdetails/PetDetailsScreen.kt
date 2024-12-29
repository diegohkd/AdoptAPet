package com.mobdao.adoptapet.screens.petdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.mobdao.adoptapet.R
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.common.layouts.PetSurface
import com.mobdao.adoptapet.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.common.theme.color.ColorSchema
import com.mobdao.adoptapet.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.screens.petdetails.PetDetailsUiAction.BackButtonClicked
import com.mobdao.adoptapet.screens.petdetails.PetDetailsUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.screens.petdetails.PetDetailsUiState.ContactState
import com.mobdao.adoptapet.screens.petdetails.PetDetailsUiState.PetCardState
import com.mobdao.adoptapet.screens.petdetails.PetDetailsUiState.PetHeaderState
import com.mobdao.adoptapet.utils.extensions.toColorSchema
import com.mobdao.domain.models.AnimalType
import com.mobdao.domain.models.AnimalType.DOG

@Composable
fun PetDetailsScreen(
    animalType: AnimalType,
    onNavAction: (PetDetailsNavAction) -> Unit,
    viewModel: PetDetailsViewModel = hiltViewModel(),
) {
    val uiState: PetDetailsUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navActionEvent: Event<PetDetailsNavAction>? by viewModel.navAction.collectAsStateWithLifecycle()

    navActionEvent?.getContentIfNotHandled()?.let(onNavAction)

    UiContent(
        animalType = animalType,
        uiState = uiState,
        onUiAction = viewModel::onUiAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UiContent(
    animalType: AnimalType = DOG,
    uiState: PetDetailsUiState = PetDetailsUiState(),
    onUiAction: (PetDetailsUiAction) -> Unit = {},
) {
    val colorSchema: ColorSchema = remember(animalType) { animalType.toColorSchema() }
    AdoptAPetTheme(colorSchema) {
        PetSurface {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { },
                        navigationIcon = {
                            IconButton(onClick = { onUiAction(BackButtonClicked) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                )
                            }
                        },
                    )
                },
            ) { internalPadding ->
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(internalPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    PetHeader(petHeader = uiState.petHeader)
                    PetCard(
                        petCard = uiState.petCard,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                    )
                    ContactCard(
                        contact = uiState.contact,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                    )
                }
            }
        }
        if (uiState.genericErrorDialogIsVisible) {
            GenericErrorDialog(onDismissGenericErrorDialog = { onUiAction(DismissGenericErrorDialog) })
        }
    }
}

@Composable
private fun PetHeader(petHeader: PetHeaderState) {
    AsyncImage(
        model = petHeader.photoUrl,
        contentDescription = null,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(400.dp),
        onState = {
            if (it is AsyncImagePainter.State.Error) {
                it.result.throwable.printStackTrace()
            }
        },
        contentScale = ContentScale.Crop,
    )
    Text(
        text = petHeader.name,
        modifier = Modifier.padding(start = 8.dp, top = 16.dp),
        fontSize = 32.sp,
    )
}

@Composable
private fun PetCard(
    petCard: PetCardState,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            VerticalField(name = stringResource(R.string.breeds), value = petCard.breed)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                VerticalField(name = stringResource(R.string.age), value = petCard.age)
                VerticalField(
                    name = stringResource(R.string.gender),
                    value = petCard.gender,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                VerticalField(name = stringResource(R.string.size), value = petCard.size)
                VerticalField(
                    name = stringResource(R.string.distance),
                    value = petCard.distance?.toString().orEmpty(),
                )
            }
            Text(
                text = petCard.description,
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
private fun ContactCard(
    contact: ContactState,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(R.string.contact), fontSize = 20.sp)
            Column(modifier = Modifier.fillMaxWidth()) {
                HorizontalField(name = stringResource(R.string.email_field), value = contact.email)
                HorizontalField(name = stringResource(R.string.phone_field), value = contact.phone)
            }
        }
    }
}

@Composable
private fun VerticalField(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = name, fontSize = 20.sp)
        Text(
            text = value,
            modifier = Modifier.padding(top = 4.dp),
            fontSize = 16.sp,
        )
    }
}

@Composable
private fun HorizontalField(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(text = name, fontSize = 20.sp)
        Text(
            text = value,
            modifier = Modifier.padding(start = 4.dp),
            fontSize = 16.sp,
        )
    }
}

@Preview
@Composable
private fun PetDetailsScreenPreview() {
    AdoptAPetTheme {
        UiContent()
    }
}
