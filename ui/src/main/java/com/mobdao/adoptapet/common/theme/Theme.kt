package com.mobdao.adoptapet.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.mobdao.adoptapet.common.theme.color.*

@Composable
fun AdoptAPetTheme(
    colorSchema: ColorSchema = DefaultColorSchema,
    content: @Composable () -> Unit
) {
    val darkTheme: Boolean = isSystemInDarkTheme()
    val colorScheme: AdoptAPetColorScheme = if (darkTheme) colorSchema.dark else colorSchema.light

    CompositionLocalProvider(LocalPetColorScheme provides colorScheme.petColorScheme) {
        MaterialTheme(
            colorScheme = colorScheme.materialColorScheme,
            typography = Typography,
            content = content
        )
    }
}

object AdoptAPetTheme {

    val petColorScheme: PetColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalPetColorScheme.current
}
