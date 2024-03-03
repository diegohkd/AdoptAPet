package com.mobdao.adoptapet.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.mobdao.adoptapet.screens.home.HomeViewModel.NavAction.PetClicked
import com.mobdao.adoptapet.screens.home.HomeViewModel.Pet
import com.mobdao.adoptapet.screens.home.HomeViewModel.UiState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onPetClicked: (id: String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navActionEvent by viewModel.navAction.collectAsStateWithLifecycle()

    when (val navAction = navActionEvent?.getContentIfNotHandled()) {
        is PetClicked -> onPetClicked(navAction.petId)
        else -> {}
    }

    HomeContent(
        uiState = uiState,
        onPetClicked = viewModel::onPetClicked,
    )
}

@Composable
private fun HomeContent(
    uiState: UiState,
    onPetClicked: (id: String) -> Unit = {},
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(uiState.pets) { pet ->
            PetItem(pet = pet, onClick = onPetClicked)
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