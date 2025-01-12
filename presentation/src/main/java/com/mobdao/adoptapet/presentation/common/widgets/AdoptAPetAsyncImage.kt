package com.mobdao.adoptapet.presentation.common.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mobdao.adoptapet.presentation.common.theme.AdoptAPetTheme

@Composable
fun AdoptAPetAsyncImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    error: Painter? = null,
    contentScale: ContentScale = ContentScale.Fit,
) {
    AsyncImage(
        model =
            ImageRequest
                .Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
        contentDescription = null,
        modifier = modifier,
        error = error,
        onError = {
            it.result.throwable.printStackTrace()
        },
        contentScale = contentScale,
    )
}

@Preview
@Composable
fun AdoptAPetAsyncImagePreview() {
    AdoptAPetTheme {
        AdoptAPetAsyncImage(imageUrl = "")
    }
}
