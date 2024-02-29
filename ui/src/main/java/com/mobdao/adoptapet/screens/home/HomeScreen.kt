package com.mobdao.adoptapet.screens.home

import androidx.compose.foundation.background
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
import com.mobdao.adoptapet.screens.home.HomeViewModel.Pet
import com.mobdao.adoptapet.screens.home.HomeViewModel.UiState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(uiState)
}

@Composable
private fun HomeContent(uiState: UiState) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(uiState.pets) { pet ->
            PetItem(pet)
        }
    }
}

@Composable
private fun PetItem(pet: Pet) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE6E6FA))
            .padding(8.dp),
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