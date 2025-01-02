package com.mobdao.adoptapet.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobdao.adoptapet.presentation.common.Event
import com.mobdao.adoptapet.presentation.common.widgets.GenericErrorDialog
import com.mobdao.adoptapet.presentation.navigation.Destination.PetDetails.PET_TYPE_ARG
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.FilterScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.OnboardingScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.OnboardingToHomeScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.PetDetailsScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.PreviousScreen
import com.mobdao.adoptapet.presentation.navigation.NavigationViewModel.NavAction.SplashToHomeScreen
import com.mobdao.adoptapet.presentation.screens.filter.FilterScreen
import com.mobdao.adoptapet.presentation.screens.home.HomeScreen
import com.mobdao.adoptapet.presentation.screens.onboarding.OnboardingScreen
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsScreen
import com.mobdao.adoptapet.presentation.screens.splash.SplashScreen
import com.mobdao.domain.models.AnimalType

@Composable
fun AdoptAPetNavHost(
    viewModel: NavigationViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val navActionEvent: Event<NavAction>? by viewModel.navAction.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val navDestination = navActionEvent?.getContentIfNotHandled()) {
        SplashToHomeScreen -> {
            val navOptions =
                NavOptions
                    .Builder()
                    .setPopUpTo(Destination.Splash.route, inclusive = true)
                    .build()
            navController.navigate(route = Destination.Home.route, navOptions = navOptions)
        }
        OnboardingScreen -> {
            val navOptions =
                NavOptions
                    .Builder()
                    .setPopUpTo(Destination.Splash.route, inclusive = true)
                    .build()
            navController.navigate(route = Destination.Onboarding.route, navOptions = navOptions)
        }
        OnboardingToHomeScreen -> {
            val navOptions =
                NavOptions
                    .Builder()
                    .setPopUpTo(Destination.Onboarding.route, inclusive = true)
                    .build()
            navController.navigate(route = Destination.Home.route, navOptions = navOptions)
        }
        is PetDetailsScreen ->
            navController.navigate(
                route =
                    Destination.PetDetails.buildRouteWithArgs(
                        petId = navDestination.petId,
                        petType = navDestination.type.name,
                    ),
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
            SplashScreen(onNavAction = viewModel::onNavAction)
        }
        composable(route = Destination.Onboarding.route) {
            OnboardingScreen(onNavAction = viewModel::onNavAction)
        }
        composable(route = Destination.Home.route) {
            HomeScreen(onNavAction = viewModel::onNavAction)
        }
        composable(
            route = Destination.PetDetails.route,
            arguments = Destination.PetDetails.arguments,
        ) {
            /*
             * TODO improve this? One solution would be creating a different screen for each animal
             *  type, but that will require a ton of boilerplate
             */
            val animalType: AnimalType =
                AnimalType.fromName(
                    name = it.arguments?.getString(PET_TYPE_ARG),
                ) ?: return@composable

            PetDetailsScreen(
                animalType = animalType,
                onNavAction = viewModel::onNavAction,
            )
        }
        composable(route = Destination.Filter.route) {
            FilterScreen(onNavAction = viewModel::onNavAction)
        }
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = viewModel::onDismissGenericErrorDialog)
    }
}
