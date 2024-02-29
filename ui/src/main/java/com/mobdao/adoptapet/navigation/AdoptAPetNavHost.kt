package com.mobdao.adoptapet.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobdao.adoptapet.screens.home.HomeScreen
import com.mobdao.adoptapet.screens.pet_details.PetDetailsScreen

@Composable
fun AdoptAPetNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destination.Home.route,
    ) {
        composable(Destination.Home.route) {
            HomeScreen()
        }
        composable(Destination.PetDetails.route) {
            PetDetailsScreen()
        }
    }
}