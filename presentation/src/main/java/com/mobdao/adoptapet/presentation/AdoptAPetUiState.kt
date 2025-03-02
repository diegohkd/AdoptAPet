package com.mobdao.adoptapet.presentation

import com.mobdao.adoptapet.presentation.navigation.Destination

data class AdoptAPetUiState(
    val isSplashVisible: Boolean = true,
    val isGenericErrorDialogVisible: Boolean = false,
    val startDestination: Destination? = null,
)
