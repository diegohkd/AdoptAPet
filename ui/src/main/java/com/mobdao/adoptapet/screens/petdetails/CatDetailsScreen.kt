package com.mobdao.adoptapet.screens.petdetails

import androidx.compose.runtime.Composable
import com.mobdao.adoptapet.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.common.theme.color.CatColorSchema
import com.mobdao.adoptapet.screens.petdetails.base.PetDetailsScreen

@Composable
fun CatDetailsScreen() {
    AdoptAPetTheme(colorSchema = CatColorSchema) {
        PetDetailsScreen()
    }
}