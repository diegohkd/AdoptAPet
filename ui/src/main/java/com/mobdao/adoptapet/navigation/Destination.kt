package com.mobdao.adoptapet.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface Destination {

    val route: String

    data object Splash : Destination {
        override val route: String = "splash"
    }

    data object Onboarding : Destination {
        override val route: String = "onboarding"
    }

    data object Home : Destination {
        override val route: String = "home"
    }

    data object PetDetails : Destination {
        const val PET_ID_ARG = "pedId"
        val arguments = listOf(
            navArgument(PET_ID_ARG) { type = NavType.StringType }
        )

        private const val HOST: String = "pet_details"
        override val route: String = "$HOST?$PET_ID_ARG={$PET_ID_ARG}"
        fun buildRouteWithArgs(petId: String): String = "$HOST?$PET_ID_ARG=$petId"
    }

    data object Filter : Destination {
        override val route: String = "filter"
    }
}
