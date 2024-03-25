package com.mobdao.adoptapet.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobdao.adoptapet.screens.splash.SplashViewModel.NavAction.Completed

@Composable
fun SplashScreen(
    onCompleted: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val navAction by viewModel.navAction.collectAsStateWithLifecycle()

    when (navAction?.getContentIfNotHandled()) {
        Completed -> onCompleted()
        null -> {}
    }

    UiContent()
}

@Composable
private fun UiContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Splash")
    }
}