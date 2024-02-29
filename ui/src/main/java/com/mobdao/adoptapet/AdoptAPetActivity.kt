package com.mobdao.adoptapet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.mobdao.adoptapet.navigation.AdoptAPetNavHost
import com.mobdao.adoptapet.common.theme.AdoptAPetTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdoptAPetApp()
        }
    }
}

@Composable
private fun AdoptAPetApp() {
    AdoptAPetTheme {
        val navController = rememberNavController()

        AdoptAPetNavHost(navController = navController)
    }
}