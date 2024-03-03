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
        composable(route = Destination.Home.route) {
            HomeScreen(
                onPetClicked = { petId ->
                    navController.navigate(
                        route = Destination.PetDetails.buildRouteWithArgs(petId)
                    )
                }
            )
        }
        composable(
            route = Destination.PetDetails.route,
            arguments = Destination.PetDetails.arguments,
        ) {
            PetDetailsScreen()
        }
    }
}