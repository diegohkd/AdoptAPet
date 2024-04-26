package com.mobdao.remote

import com.mobdao.domain.entities.Pet
import com.mobdao.remote.internal.mappers.EntityMapper
import com.mobdao.remote.internal.services.PetFinderService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalRemoteDataSource @Inject internal constructor(
    private val petFinderService: PetFinderService,
    private val entityMapper: EntityMapper,
) {

    data class GeoCoordinates(val latitude: Double, val longitude: Double)

    suspend fun getPets(
        pageNumber: Int,
        locationCoordinates: GeoCoordinates?,
        animalType: String?
    ): List<Pet> {
        val location = locationCoordinates?.let {
            "${it.latitude},${it.longitude}"
        }
        return petFinderService.getAnimals(
            pageNumber = pageNumber,
            location = location,
            type = animalType,
        ).animals.let(entityMapper::toPets)
    }
}
