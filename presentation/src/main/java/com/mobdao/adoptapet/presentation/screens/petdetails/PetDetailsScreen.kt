@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.mobdao.adoptapet.presentation.screens.petdetails

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobdao.adoptapet.domain.models.AnimalType
import com.mobdao.adoptapet.domain.models.AnimalType.DOG
import com.mobdao.adoptapet.presentation.R
import com.mobdao.adoptapet.presentation.common.Event
import com.mobdao.adoptapet.presentation.common.layouts.PetSurface
import com.mobdao.adoptapet.presentation.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.presentation.common.theme.color.ColorSchema
import com.mobdao.adoptapet.presentation.common.utils.extensions.toColorSchema
import com.mobdao.adoptapet.presentation.common.widgets.AdoptAPetAsyncImage
import com.mobdao.adoptapet.presentation.common.widgets.BackButton
import com.mobdao.adoptapet.presentation.common.widgets.DetailCard
import com.mobdao.adoptapet.presentation.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.presentation.common.widgets.PetBackgroundCard
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiAction.BackButtonClicked
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiState.ContactState
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiState.PetDetailsCardState
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiState.PetHeaderState

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
    uiState: PetDetailsUiState,
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
                            BackButton(onClicked = { onUiAction(BackButtonClicked) })
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

                    PetDetailsCard(
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
    PetBackgroundCard(modifier = Modifier.padding(start = 20.dp, top = 8.dp, end = 20.dp)) {
        DetailCard(modifier = Modifier.padding(16.dp)) {
            AdoptAPetAsyncImage(
                imageUrl = petHeader.photoUrl,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                contentScale = ContentScale.Crop,
            )
        }
    }
    Text(
        text = petHeader.name,
        modifier = Modifier.padding(start = 8.dp, top = 16.dp),
        fontSize = 32.sp,
    )
}

@Composable
private fun PetDetailsCard(
    petCard: PetDetailsCardState,
    modifier: Modifier = Modifier,
) {
    PetBackgroundCard(modifier = modifier) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            VerticalField(
                name = stringResource(R.string.breeds),
                value = petCard.breed,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                VerticalField(
                    name = stringResource(R.string.age),
                    value = petCard.age,
                    modifier = Modifier.weight(1f),
                )
                VerticalField(
                    name = stringResource(R.string.gender),
                    value = petCard.gender,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(
                modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                VerticalField(
                    name = stringResource(R.string.size),
                    value = petCard.size,
                    modifier = Modifier.weight(1f),
                )
                VerticalField(
                    name = stringResource(R.string.distance),
                    value = stringResource(R.string.miles, petCard.distance?.toString().orEmpty()),
                    modifier = Modifier.weight(1f),
                )
            }

            if (petCard.description.isNotBlank()) {
                DetailCard {
                    Text(
                        text = petCard.description,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactCard(
    contact: ContactState,
    modifier: Modifier = Modifier,
) {
    PetBackgroundCard(modifier = modifier) {
        DetailCard(modifier = modifier) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(R.string.contact), fontSize = 20.sp, fontWeight = Bold)
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalField(
                        name = stringResource(R.string.email_field),
                        value = contact.email,
                    )
                    HorizontalField(
                        name = stringResource(R.string.phone_field),
                        value = contact.phone,
                    )
                }
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
    DetailCard(modifier = modifier.fillMaxHeight()) {
        Column(
            modifier =
                modifier
                    .padding(8.dp)
                    .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = name, fontSize = 20.sp, fontWeight = Bold)
            Text(
                text = value,
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
private fun HorizontalField(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(text = name, fontSize = 20.sp, fontWeight = Bold)
        Text(
            text = value,
            modifier = Modifier.padding(start = 4.dp),
            fontSize = 16.sp,
        )
    }
}

@Preview(heightDp = 1100)
@Composable
private fun PetDetailsScreenPreview() {
    AdoptAPetTheme {
        UiContent(
            uiState =
                PetDetailsUiState(
                    petHeader =
                        PetHeaderState(
                            photoUrl = "",
                            name = "Bob",
                        ),
                    petCard =
                        PetDetailsCardState(
                            breed = "Bulldog",
                            age = "Baby",
                            gender = "Male",
                            size = "Medium",
                            distance = 44.38f,
                            description = LoremIpsum(10).values.first(),
                        ),
                ),
        )
    }
}

@Preview(heightDp = 1100, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PetDetailsScreen2Preview() {
    AdoptAPetTheme {
        UiContent(
            uiState =
                PetDetailsUiState(
                    petHeader =
                        PetHeaderState(
                            photoUrl = "",
                            name = "Bob",
                        ),
                    petCard =
                        PetDetailsCardState(
                            breed = "Bulldog",
                            age = "Baby",
                            gender = "Male",
                            size = "Medium",
                            distance = 44.38f,
                            description = LoremIpsum(10).values.first(),
                        ),
                ),
        )
    }
}
