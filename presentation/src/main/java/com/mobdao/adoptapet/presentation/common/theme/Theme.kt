package com.mobdao.adoptapet.presentation.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.toArgb
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.test.FakeImage
import com.mobdao.adoptapet.presentation.common.theme.color.AdoptAPetColorScheme
import com.mobdao.adoptapet.presentation.common.theme.color.ColorSchema
import com.mobdao.adoptapet.presentation.common.theme.color.DefaultColorSchema
import com.mobdao.adoptapet.presentation.common.theme.color.LocalPetColorScheme
import com.mobdao.adoptapet.presentation.common.theme.color.PetColorScheme

@OptIn(ExperimentalCoilApi::class)
@Composable
fun AdoptAPetTheme(
    colorSchema: ColorSchema = DefaultColorSchema,
    content: @Composable () -> Unit,
) {
    val darkTheme: Boolean = isSystemInDarkTheme()
    val colorScheme: AdoptAPetColorScheme = if (darkTheme) colorSchema.dark else colorSchema.light
    val asyncImagePreviewHandler =
        AsyncImagePreviewHandler { FakeImage(color = LightGray.toArgb()) }

    CompositionLocalProvider(
        LocalPetColorScheme provides colorScheme.petColorScheme,
        LocalAsyncImagePreviewHandler provides asyncImagePreviewHandler,
    ) {
        MaterialTheme(
            colorScheme = colorScheme.materialColorScheme,
            typography = Typography,
            content = content,
        )
    }
}

object AdoptAPetTheme {
    val petColorScheme: PetColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalPetColorScheme.current
}
