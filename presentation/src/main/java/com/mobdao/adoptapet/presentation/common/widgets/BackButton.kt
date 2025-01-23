package com.mobdao.adoptapet.presentation.common.widgets

import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClicked,
    ) {
        Icon(
            imageVector = Filled.ArrowBack,
            contentDescription = "Back",
        )
    }
}
