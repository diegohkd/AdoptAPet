package com.mobdao.adoptapet.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mobdao.adoptapet.R
import com.mobdao.adoptapet.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.screens.splash.SplashViewModel.NavAction

@Composable
fun SplashScreen(
    onNavAction: (NavAction) -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val navAction by viewModel.navAction.collectAsStateWithLifecycle()

    navAction?.getContentIfNotHandled()?.let(onNavAction)

    UiContent()
}

@Composable
private fun UiContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val composition: LottieComposition? by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.splash_animation)
        )
        LottieAnimation(
            composition = composition,
            modifier = Modifier.width(200.dp),
            iterations = LottieConstants.IterateForever
        )
    }
}

@Composable
@Preview
private fun UiContentPreview() {
    AdoptAPetTheme {
        UiContent()
    }
}