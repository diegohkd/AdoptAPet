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

    sealed class PetDetails : Destination {
        companion object {
            const val PET_ID_ARG = "pedId"
        }

        val arguments = listOf(
            navArgument(PET_ID_ARG) { type = NavType.StringType }
        )
        protected abstract val host: String

        override val route: String get() = "${host}?$PET_ID_ARG={$PET_ID_ARG}"
        fun buildRouteWithArgs(petId: String): String = "$host?$PET_ID_ARG=$petId"

        data object Cat : PetDetails() {
            override val host: String = "cat_details"
        }

        data object Dog : PetDetails() {
            override val host: String = "dog_details"
        }

        data object Rabbit : PetDetails() {
            override val host: String = "rabbit_details"
        }
    }

    data object Filter : Destination {
        override val route: String = "filter"
    }
}
