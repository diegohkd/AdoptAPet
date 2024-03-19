package com.mobdao.remote

import com.mobdao.remote.responses.Animal
import com.mobdao.remote.services.PetFinderService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalRemoteDataSource @Inject internal constructor(
    private val petFinderService: PetFinderService,
) {

    data class GeoCoordinates(val latitude: Double, val longitude: Double)

    suspend fun getAnimals(
        pageNumber: Int,
        // TODO create value-object
        locationCoordinates: GeoCoordinates?,
        animalType: String?
    ): List<Animal> {
        val location = locationCoordinates?.let {
            "${it.latitude},${it.longitude}"
        }
        return petFinderService.getAnimals(
            pageNumber = pageNumber,
            location = location,
            type = animalType,
        ).animals
    }
}