package com.mobdao.adoptapet.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface Destination {

   val route: String

    data object Home : Destination {
        override val route: String = "home"
    }

    data object PetDetails : Destination {
        const val petIdArg = "pedId"
        val arguments = listOf(
            navArgument(petIdArg) { type = NavType.StringType }
        )

        private const val host: String = "pet_details"
        override val route: String = "$host?$petIdArg={$petIdArg}"
        fun buildRouteWithArgs(petId: String): String = "$host?$petIdArg=$petId"
    }
}