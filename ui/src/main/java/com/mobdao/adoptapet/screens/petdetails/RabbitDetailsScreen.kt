package com.mobdao.adoptapet.screens.petdetails

import androidx.compose.runtime.Composable
import com.mobdao.adoptapet.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.common.theme.color.RabbitColorSchema
import com.mobdao.adoptapet.screens.petdetails.base.PetDetailsScreen

@Composable
fun RabbitDetailsScreen() {
    AdoptAPetTheme(colorSchema = RabbitColorSchema) {
        PetDetailsScreen()
    }
}