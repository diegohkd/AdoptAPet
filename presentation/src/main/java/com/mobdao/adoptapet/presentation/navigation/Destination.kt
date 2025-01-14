package com.mobdao.adoptapet.presentation.navigation

import com.mobdao.adoptapet.domain.models.AnimalType
import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object Splash : Destination

    @Serializable
    data object Onboarding : Destination

    @Serializable
    data object Home : Destination

    @Serializable
    data class PetDetails(
        val petId: String,
        val petType: AnimalType,
    ) : Destination

    @Serializable
    data object Filter : Destination
}
