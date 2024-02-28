package com.mobdao.adoptapet.navigation

sealed interface Destination {

   val route: String

    object Home : Destination {
        override val route: String = "home"
    }

    object PetDetails : Destination {
        override val route: String = "pet_details"
    }
}