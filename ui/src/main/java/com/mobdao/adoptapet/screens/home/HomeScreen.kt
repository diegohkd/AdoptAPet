package com.mobdao.adoptapet.screens.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mobdao.adoptapet.R
import com.mobdao.adoptapet.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.screens.home.HomeViewModel.NavAction.FilterClicked
import com.mobdao.adoptapet.screens.home.HomeViewModel.NavAction.PetClicked
import com.mobdao.adoptapet.screens.home.HomeViewModel.Pet
import com.mobdao.adoptapet.screens.home.HomeViewModel.UiState
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onPetClicked: (id: String) -> Unit,
    onFilterClicked: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState.processLocationPermission) {
        val locationPermissionState = rememberMultiplePermissionsState(
            permissions = listOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
        )
        LaunchedEffect(
            key1 = locationPermissionState.allPermissionsGranted,
            key2 = locationPermissionState.shouldShowRationale
        ) {
            viewModel.onLocationPermissionStateUpdated(
                areAllLocationPermissionsGranted = locationPermissionState.allPermissionsGranted,
                shouldShowRationale = locationPermissionState.shouldShowRationale,
            )
        }
        val askLocationPermissionEvent by viewModel.askLocationPermission.collectAsStateWithLifecycle()
        askLocationPermissionEvent?.getContentIfNotHandled()?.let {
            locationPermissionState.launchMultiplePermissionRequest()
        }
    }
    val petsPagingItems: LazyPagingItems<Pet> = viewModel.items.collectAsLazyPagingItems()
    val navActionEvent by viewModel.navAction.collectAsStateWithLifecycle()

    when (val navAction = navActionEvent?.getContentIfNotHandled()) {
        is PetClicked -> onPetClicked(navAction.petId)
        FilterClicked -> onFilterClicked()
        null -> {}
    }

    viewModel.onPetsListLoadStateUpdate(
        refreshLoadState = petsPagingItems.loadState.refresh,
        appendLoadState = petsPagingItems.loadState.append,
    )

    HomeContent(
        uiState = uiState,
        petsPagingItems = petsPagingItems,
        onPetClicked = viewModel::onPetClicked,
        onFilterClicked = viewModel::onFilterClicked,
    )
}

// TODO check if passing LazyPagingItems causes extra recompositions
@Composable
private fun HomeContent(
    uiState: UiState,
    petsPagingItems: LazyPagingItems<Pet>,
    onPetClicked: (id: String) -> Unit = {},
    onFilterClicked: () -> Unit = {},
) {
    Scaffold(
        containerColor = Color.White
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize()
        ) {
            val (toolbarRef, petListRef, progressIndicatorRef) = createRefs()

            ToolBar(
                address = uiState.address,
                onFilterClicked = onFilterClicked,
                modifier = Modifier.constrainAs(toolbarRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(petListRef) {
                        start.linkTo(parent.start)
                        top.linkTo(toolbarRef.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
            ) {
                items(petsPagingItems.itemCount) { index ->
                    PetItem(pet = petsPagingItems[index]!!, onClick = onPetClicked)
                }
                if (uiState.nextPageProgressIndicatorIsVisible) {
                    item {
                        NextPageProgressIndicator(
                            modifier = Modifier.constrainAs(progressIndicatorRef) {
                                centerTo(parent)
                            }
                        )
                    }
                }
            }
            if (uiState.progressIndicatorIsVisible) {
                CircularProgressIndicator(
                    modifier = Modifier.constrainAs(progressIndicatorRef) {
                        centerTo(parent)
                    }
                )
            }
        }
    }
}

@Composable
private fun ToolBar(
    address: String,
    onFilterClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        val (locationIconRef, locationTextRef, filterRef) = createRefs()
        Icon(
            painter = painterResource(id = R.drawable.location_ic),
            contentDescription = "",
            modifier = Modifier
                .constrainAs(locationIconRef) {
                    start.linkTo(parent.start)
                    centerVerticallyTo(parent)
                }
                .padding(start = 8.dp),
            tint = Color.Black
        )
        Text(
            text = address,
            modifier = Modifier
                .constrainAs(locationTextRef) {
                    start.linkTo(locationIconRef.end)
                    centerVerticallyTo(parent)
                    end.linkTo(filterRef.start)
                    width = Dimension.fillToConstraints
                }
                .padding(horizontal = 8.dp),
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        IconButton(
            onClick = onFilterClicked,
            modifier = Modifier
                .constrainAs(filterRef) {
                    centerVerticallyTo(parent)
                    end.linkTo(parent.end)
                }
                .padding(end = 8.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.filter_ic),
                contentDescription = "",
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun PetItem(pet: Pet, onClick: (id: String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE6E6FA))
            .padding(8.dp)
            .clickable { onClick(pet.id) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = pet.thumbnailUrl,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            onState = {
                if (it is AsyncImagePainter.State.Error) {
                    it.result.throwable.printStackTrace()
                }
            },
            contentScale = ContentScale.Crop,
        )
        Text(
            text = pet.name,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun NextPageProgressIndicator(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFE6E6FA))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun HomeContentPreview() {
    AdoptAPetTheme {
        HomeContent(
            uiState = UiState(
                address = "Av Dr Esmerino Ribeiro do Valle, 680, Nova Floresta",
            ),
            petsPagingItems = flowOf(
                PagingData.from(
                    data = listOf(
                        Pet(
                            id = "id-1",
                            name = "Bibico",
                            thumbnailUrl = "",
                        ),
                        Pet(
                            id = "id-2",
                            name = "Nina",
                            thumbnailUrl = "",
                        ),
                        Pet(
                            id = "id-3",
                            name = "Nilla",
                            thumbnailUrl = "",
                        )
                    ),
                    sourceLoadStates = LoadStates(
                        refresh = LoadState.NotLoading(false),
                        append = LoadState.NotLoading(false),
                        prepend = LoadState.NotLoading(false),
                    ),
                )
            ).collectAsLazyPagingItems()
        )
    }
}