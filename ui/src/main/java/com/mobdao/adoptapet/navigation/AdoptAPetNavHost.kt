package com.mobdao.adoptapet.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.navigation.NavigationViewModel.NavAction
import com.mobdao.adoptapet.navigation.NavigationViewModel.NavAction.*
import com.mobdao.adoptapet.screens.filter.FilterScreen
import com.mobdao.adoptapet.screens.home.HomeScreen
import com.mobdao.adoptapet.screens.onboarding.OnboardingScreen
import com.mobdao.adoptapet.screens.petdetails.CatDetailsScreen
import com.mobdao.adoptapet.screens.petdetails.DogDetailsScreen
import com.mobdao.adoptapet.screens.petdetails.RabbitDetailsScreen
import com.mobdao.adoptapet.screens.splash.SplashScreen

@Composable
fun AdoptAPetNavHost(
    viewModel: NavigationViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val navActionEvent: Event<NavAction>? by viewModel.navAction.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val navDestination = navActionEvent?.getContentIfNotHandled()) {
        SplashToHomeScreen -> {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(Destination.Splash.route, inclusive = true)
                .build()
            navController.navigate(route = Destination.Home.route, navOptions = navOptions)
        }
        OnboardingScreen -> {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(Destination.Splash.route, inclusive = true)
                .build()
            navController.navigate(route = Destination.Onboarding.route, navOptions = navOptions)
        }
        OnboardingToHomeScreen -> {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(Destination.Onboarding.route, inclusive = true)
                .build()
            navController.navigate(route = Destination.Home.route, navOptions = navOptions)
        }
        is CatDetailsScreen ->
            navController.navigate(
                route = Destination.PetDetails.Cat.buildRouteWithArgs(navDestination.petId)
            )
        is DogDetailsScreen ->
            navController.navigate(
                route = Destination.PetDetails.Dog.buildRouteWithArgs(navDestination.petId)
            )
        is RabbitDetailsScreen ->
            navController.navigate(
                route = Destination.PetDetails.Rabbit.buildRouteWithArgs(navDestination.petId)
            )
        FilterScreen -> navController.navigate(route = Destination.Filter.route)
        PreviousScreen -> navController.popBackStack()
        null -> {}
    }

    NavHost(
        navController = navController,
        startDestination = Destination.Splash.route,
    ) {
        composable(route = Destination.Splash.route) {
            SplashScreen(onCompleted = viewModel::onSplashCompleted)
        }
        composable(route = Destination.Onboarding.route) {
            OnboardingScreen(onCompleted = viewModel::onOnboardingCompleted)
        }
        composable(route = Destination.Home.route) {
            HomeScreen(onNavAction = viewModel::onHomeNavAction)
        }
        composable(
            route = Destination.PetDetails.Cat.route,
            arguments = Destination.PetDetails.Cat.arguments,
        ) {
            CatDetailsScreen()
        }
        composable(
            route = Destination.PetDetails.Dog.route,
            arguments = Destination.PetDetails.Dog.arguments,
        ) {
            DogDetailsScreen()
        }
        composable(
            route = Destination.PetDetails.Rabbit.route,
            arguments = Destination.PetDetails.Rabbit.arguments,
        ) {
            RabbitDetailsScreen()
        }
        composable(route = Destination.Filter.route) {
            FilterScreen(onFilterApplied = viewModel::onFilterApplied)
        }
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = viewModel::onDismissGenericErrorDialog)
    }
}
