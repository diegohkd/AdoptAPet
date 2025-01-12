package com.mobdao.adoptapet.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mobdao.adoptapet.domain.models.AnimalType.CAT
import com.mobdao.adoptapet.domain.models.AnimalType.DOG
import com.mobdao.adoptapet.domain.models.AnimalType.RABBIT
import com.mobdao.adoptapet.presentation.R
import com.mobdao.adoptapet.presentation.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.presentation.common.theme.color.ColorSchema
import com.mobdao.adoptapet.presentation.common.widgets.AdoptAPetAsyncImage
import com.mobdao.adoptapet.presentation.common.widgets.DetailCard
import com.mobdao.adoptapet.presentation.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.presentation.common.widgets.PetBackgroundCard
import com.mobdao.adoptapet.presentation.screens.home.HomeUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.screens.home.HomeUiAction.FilterClicked
import com.mobdao.adoptapet.presentation.screens.home.HomeUiAction.PetClicked
import com.mobdao.adoptapet.presentation.screens.home.HomeUiState.BreedsState
import com.mobdao.adoptapet.presentation.screens.home.HomeUiState.PetState
import com.mobdao.adoptapet.presentation.utils.extensions.toColorSchema
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavAction: (HomeNavAction) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val petsPagingItems: LazyPagingItems<PetState> = viewModel.items.collectAsLazyPagingItems()
    val navActionEvent by viewModel.navAction.collectAsStateWithLifecycle()

    navActionEvent?.getContentIfNotHandled()?.let(onNavAction)

    LaunchedEffect(
        petsPagingItems.loadState.refresh,
        petsPagingItems.loadState.append,
        petsPagingItems.itemCount,
    ) {
        viewModel.onPetsListLoadStateUpdate(
            refreshLoadState = petsPagingItems.loadState.refresh,
            appendLoadState = petsPagingItems.loadState.append,
            itemsCount = petsPagingItems.itemCount,
        )
    }

    UiContent(
        uiState = uiState,
        petsPagingItems = petsPagingItems,
        onUiAction = viewModel::onUiAction,
    )
}

// TODO check if passing LazyPagingItems causes extra recompositions
@Composable
private fun UiContent(
    uiState: HomeUiState,
    petsPagingItems: LazyPagingItems<PetState>,
    onUiAction: (HomeUiAction) -> Unit = {},
) {
    ConstraintLayout(
        modifier =
            Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
    ) {
        val (
            toolbarRef,
            petListRef,
            progressIndicatorRef,
            emptyListPlaceholderRef,
        ) = createRefs()

        ToolBar(
            address = uiState.address,
            onFilterClicked = { onUiAction(FilterClicked) },
            modifier =
                Modifier.constrainAs(toolbarRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
        )
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .constrainAs(petListRef) {
                        start.linkTo(parent.start)
                        top.linkTo(toolbarRef.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(petsPagingItems.itemCount) { index ->
                PetItem(
                    pet = petsPagingItems[index]!!,
                    onClick = { petState -> onUiAction(PetClicked(petState)) },
                )
            }
            if (uiState.nextPageProgressIndicatorIsVisible) {
                item {
                    NextPageProgressIndicator(
                        modifier =
                            Modifier.constrainAs(progressIndicatorRef) {
                                centerTo(parent)
                            },
                    )
                }
            }
        }
        if (uiState.progressIndicatorIsVisible) {
            CircularProgressIndicator(
                modifier =
                    Modifier.constrainAs(progressIndicatorRef) {
                        centerTo(parent)
                    },
            )
        }
        if (uiState.emptyListPlaceholderIsVisible) {
            EmptyListPlaceholder(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .constrainAs(emptyListPlaceholderRef) {
                            start.linkTo(parent.start)
                            top.linkTo(toolbarRef.bottom)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        },
                onFilterClicked = { onUiAction(FilterClicked) },
            )
        }
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = { onUiAction(DismissGenericErrorDialog) })
    }
}

@Composable
private fun ToolBar(
    address: String,
    onFilterClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier =
            modifier
                .fillMaxWidth()
                .height(56.dp),
    ) {
        val (locationIconRef, locationTextRef, filterRef) = createRefs()
        Icon(
            painter = painterResource(id = R.drawable.location_ic),
            contentDescription = "",
            modifier =
                Modifier
                    .constrainAs(locationIconRef) {
                        start.linkTo(parent.start)
                        centerVerticallyTo(parent)
                    }.padding(start = 8.dp),
        )
        Text(
            text = address,
            modifier =
                Modifier
                    .constrainAs(locationTextRef) {
                        start.linkTo(locationIconRef.end)
                        centerVerticallyTo(parent)
                        end.linkTo(filterRef.start)
                        width = Dimension.fillToConstraints
                    }.padding(horizontal = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        IconButton(
            onClick = onFilterClicked,
            modifier =
                Modifier
                    .constrainAs(filterRef) {
                        centerVerticallyTo(parent)
                        end.linkTo(parent.end)
                    }.padding(end = 8.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.filter_ic),
                contentDescription = "",
            )
        }
    }
}

@Composable
private fun PetItem(
    pet: PetState,
    onClick: (pet: PetState) -> Unit,
) {
    val colorSchema: ColorSchema = remember(pet.type) { pet.type.toColorSchema() }
    AdoptAPetTheme(colorSchema = colorSchema) {
        PetBackgroundCard {
            Row(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(intrinsicSize = IntrinsicSize.Max)
                        .clickable { onClick(pet) },
            ) {
                DetailCard {
                    AdoptAPetAsyncImage(
                        imageUrl = pet.thumbnailUrl,
                        modifier = Modifier.size(110.dp),
                        error = painterResource(id = R.drawable.paw_ic),
                        contentScale = ContentScale.Crop,
                    )
                }

                DetailCard(
                    modifier =
                        Modifier
                            .padding(start = 8.dp)
                            .fillMaxSize(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = pet.name,
                            maxLines = 1,
                            fontSize = 18.sp,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = pet.formattedBreeds(),
                            maxLines = 1,
                            fontSize = 12.sp,
                            overflow = TextOverflow.Ellipsis,
                        )
                        pet.distance?.let {
                            Text(
                                text = stringResource(R.string.miles, it.toString()),
                                maxLines = 1,
                                fontSize = 12.sp,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PetItemPreview() {
    AdoptAPetTheme {
        PetItem(
            pet =
                PetState(
                    id = "id-1",
                    type = DOG,
                    name = "Bibico",
                    breeds = BreedsState("SRD", ""),
                    thumbnailUrl = "",
                ),
            onClick = {},
        )
    }
}

@Composable
private fun NextPageProgressIndicator(modifier: Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyListPlaceholder(
    modifier: Modifier,
    onFilterClicked: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.no_pets_found),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onFilterClicked,
            modifier = Modifier.padding(top = 8.dp),
        ) {
            Text(text = stringResource(R.string.update_filters))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    AdoptAPetTheme {
        UiContent(
            uiState =
                HomeUiState(
                    address = "Av Dr Esmerino Ribeiro do Valle, 680, Nova Floresta",
                ),
            petsPagingItems =
                flowOf(
                    PagingData.from(
                        data =
                            listOf(
                                PetState(
                                    id = "id-1",
                                    type = DOG,
                                    name = "Bibico",
                                    breeds = BreedsState("SRD", ""),
                                    thumbnailUrl = "",
                                ),
                                PetState(
                                    id = "id-2",
                                    type = DOG,
                                    name = "Nina",
                                    breeds = BreedsState("SRD", ""),
                                    thumbnailUrl = "",
                                ),
                                PetState(
                                    id = "id-3",
                                    type = CAT,
                                    name = "Nilla",
                                    breeds = BreedsState("SRD", ""),
                                    thumbnailUrl = "",
                                ),
                                PetState(
                                    id = "id-4",
                                    type = RABBIT,
                                    name = "Pepê",
                                    breeds = BreedsState("SRD", ""),
                                    thumbnailUrl = "",
                                ),
                            ),
                        sourceLoadStates =
                            LoadStates(
                                refresh = LoadState.NotLoading(false),
                                append = LoadState.NotLoading(false),
                                prepend = LoadState.NotLoading(false),
                            ),
                    ),
                ).collectAsLazyPagingItems(),
        )
    }
}

// TODO move this to the ViewModel layer
private fun PetState.formattedBreeds(): String =
    if (!breeds.primary.isNullOrBlank()) {
        "${breeds.primary}" + (breeds.secondary?.takeIf { it.isNotBlank() }?.let { " & $it" } ?: "")
    } else {
        ""
    }
