package com.mobdao.adoptapet.screens.petdetails

import androidx.compose.runtime.Composable
import com.mobdao.adoptapet.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.common.theme.color.DogColorSchema
import com.mobdao.adoptapet.screens.petdetails.base.PetDetailsScreen

@Composable
fun DogDetailsScreen() {
    AdoptAPetTheme(colorSchema = DogColorSchema) {
        PetDetailsScreen()
    }
}