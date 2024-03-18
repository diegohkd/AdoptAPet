package com.mobdao.remote

import com.mobdao.remote.responses.Animal
import com.mobdao.remote.services.PetFinderService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalRemoteDataSource @Inject internal constructor(
    private val petFinderService: PetFinderService,
) {

    suspend fun getAnimals(
        pageNumber: Int,
        // TODO create value-object
        formattedLocationCoordinates: String?,
        animalType: String?
    ): List<Animal> {
        return petFinderService.getAnimals(
            pageNumber = pageNumber,
            location = formattedLocationCoordinates,
            type = animalType,
        ).animals
    }
}