package com.mobdao.adoptapet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.mobdao.adoptapet.common.theme.AdoptAPetTheme
import com.mobdao.adoptapet.common.theme.color.DefaultColorSchema
import com.mobdao.adoptapet.navigation.AdoptAPetNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdoptAPetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AdoptAPetApp()
        }
    }
}

@Composable
private fun AdoptAPetApp() {
    AdoptAPetTheme {
        Surface {
            val navController = rememberNavController()

            AdoptAPetNavHost(navController = navController)
        }
    }
}
