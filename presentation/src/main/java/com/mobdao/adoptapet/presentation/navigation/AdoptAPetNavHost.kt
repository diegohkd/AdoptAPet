package com.mobdao.adoptapet.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mobdao.adoptapet.domain.models.AnimalType
import com.mobdao.adoptapet.presentation.common.Event
import com.mobdao.adoptapet.presentation.common.utils.extensions.composableHorizontalAnimated
import com.mobdao.adoptapet.presentation.common.widgets.GenericErrorDialog
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
                    .setPopUpTo(route = Destination.Splash, inclusive = true)
                    .build()
            navController.navigate(route = Destination.Home, navOptions = navOptions)
        }
        OnboardingScreen -> {
            val navOptions =
                NavOptions
                    .Builder()
                    .setPopUpTo(route = Destination.Splash, inclusive = true)
                    .build()
            navController.navigate(route = Destination.Onboarding, navOptions = navOptions)
        }
        OnboardingToHomeScreen -> {
            val navOptions =
                NavOptions
                    .Builder()
                    .setPopUpTo(route = Destination.Onboarding, inclusive = true)
                    .build()
            navController.navigate(route = Destination.Home, navOptions = navOptions)
        }
        is PetDetailsScreen ->
            navController.navigate(
                route =
                    Destination.PetDetails(
                        petId = navDestination.petId,
                        petType = navDestination.type,
                    ),
            )
        FilterScreen -> navController.navigate(route = Destination.Filter)
        PreviousScreen -> navController.popBackStack()
        null -> {}
    }

    NavHost(
        navController = navController,
        startDestination = Destination.Splash,
    ) {
        composable<Destination.Splash> {
            SplashScreen(onNavAction = viewModel::onNavAction)
        }
        composable<Destination.Onboarding> {
            OnboardingScreen(onNavAction = viewModel::onNavAction)
        }
        composable<Destination.Home> {
            HomeScreen(onNavAction = viewModel::onNavAction)
        }
        composableHorizontalAnimated<Destination.PetDetails> {
            /*
             * TODO improve this? One solution would be creating a different screen for each animal
             *  type, but that will require a ton of boilerplate
             */
            val animalType: AnimalType = it.toRoute<Destination.PetDetails>().petType

            PetDetailsScreen(
                animalType = animalType,
                onNavAction = viewModel::onNavAction,
            )
        }
        composableHorizontalAnimated<Destination.Filter> {
            FilterScreen(onNavAction = viewModel::onNavAction)
        }
    }

    if (uiState.genericErrorDialogIsVisible) {
        GenericErrorDialog(onDismissGenericErrorDialog = viewModel::onDismissGenericErrorDialog)
    }
}
