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
        const val PET_TYPE_ARG = "pedType"
        val arguments =
            listOf(
                navArgument(PET_ID_ARG) { type = NavType.StringType },
            )

        private const val HOST: String = "pet_details"
        override val route: String = "$HOST?$PET_ID_ARG={$PET_ID_ARG}&$PET_TYPE_ARG={$PET_TYPE_ARG}"

        fun buildRouteWithArgs(
            petId: String,
            petType: String,
        ): String = "$HOST?$PET_ID_ARG=$petId&$PET_TYPE_ARG=$petType"
    }

    data object Filter : Destination {
        override val route: String = "filter"
    }
}
