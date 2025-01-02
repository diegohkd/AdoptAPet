package com.mobdao.adoptapet.presentation.common.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class PetColorScheme(
    val background: Color = Color.Unspecified,
)

@Immutable
data class AdoptAPetColorScheme(
    val petColorScheme: PetColorScheme,
    val materialColorScheme: ColorScheme,
)

@Immutable
data class ColorSchema(
    val light: AdoptAPetColorScheme,
    val dark: AdoptAPetColorScheme,
)

val LocalPetColorScheme: ProvidableCompositionLocal<PetColorScheme> =
    staticCompositionLocalOf {
        PetColorScheme()
    }
