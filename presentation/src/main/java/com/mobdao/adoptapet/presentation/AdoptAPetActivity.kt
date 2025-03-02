package com.mobdao.adoptapet.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.mobdao.adoptapet.presentation.AdoptAPetUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.presentation.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.presentation.navigation.AdoptAPetNavHost
import com.mobdao.adoptapet.presentation.navigation.Destination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdoptAPetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen: SplashScreen = installSplashScreen()
        enableEdgeToEdge()

        setContent {
            val viewModel: AdoptAPetViewModel = hiltViewModel()
            val uiState: AdoptAPetUiState by viewModel.uiState.collectAsStateWithLifecycle()

            when {
                uiState.isSplashVisible -> {
                    splashScreen.setKeepOnScreenCondition { true }
                }
                uiState.startDestination != null -> {
                    splashScreen.setKeepOnScreenCondition { false }
                    AdoptAPetApp(uiState.startDestination!!)
                }
            }
            if (uiState.isGenericErrorDialogVisible) {
                GenericErrorDialog(
                    onDismissGenericErrorDialog = { viewModel.onUiAction(action = DismissGenericErrorDialog) },
                )
            }
        }
    }
}

@Composable
private fun AdoptAPetApp(startDestination: Destination) {
    AdoptAPetTheme {
        Surface {
            val navController = rememberNavController()

            AdoptAPetNavHost(
                startDestination = startDestination,
                navController = navController,
            )
        }
    }
}
