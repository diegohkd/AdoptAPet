package com.mobdao.adoptapet.presentation.common.layouts

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.mobdao.adoptapet.presentation.common.theme.AdoptAPetTheme

@Composable
fun PetSurface(content: @Composable () -> Unit) {
    Surface(color = AdoptAPetTheme.petColorScheme.background, content = content)
}
