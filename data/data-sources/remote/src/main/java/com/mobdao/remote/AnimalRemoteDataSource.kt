package com.mobdao.remote

import com.mobdao.adoptapet.domain.entities.PetEntity
import com.mobdao.adoptapet.domain.entities.PetGenderEntity
import com.mobdao.adoptapet.domain.entities.PetTypeEntity
import com.mobdao.remote.internal.services.PetFinderService
import com.mobdao.remote.internal.utils.mappers.EntityMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalRemoteDataSource @Inject internal constructor(
    private val petFinderService: PetFinderService,
    private val entityMapper: EntityMapper,
) {
    data class GeoCoordinates(
        val latitude: Double,
        val longitude: Double,
    )

    suspend fun getPets(
        pageNumber: Int,
        locationCoordinates: GeoCoordinates?,
        petType: PetTypeEntity?,
        petGenders: List<PetGenderEntity>,
    ): List<PetEntity> {
        val location =
            locationCoordinates?.let {
                listOf(it.latitude, it.longitude).joinToString(",")
            }
        val genders: String? =
            petGenders
                .joinToString(",") { entityMapper.toGender(it).rawValue }
                .takeIf { it.isNotBlank() }
        return petFinderService
            .getAnimals(
                pageNumber = pageNumber,
                location = location,
                type = petType?.let(entityMapper::toAnimalType)?.rawValue,
                gender = genders,
            ).animals
            .let(entityMapper::toPets)
    }
}
