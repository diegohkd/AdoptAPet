package com.mobdao.adoptapet.presentation.common.widgets

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobdao.adoptapet.presentation.common.theme.AdoptAPetTheme

@Composable
fun PetBackgroundCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        colors =
            CardDefaults
                .cardColors()
                .copy(containerColor = AdoptAPetTheme.petColorScheme.petCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        content = content,
    )
}
