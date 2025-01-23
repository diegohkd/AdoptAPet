package com.mobdao.data.repositories

import com.mobdao.adoptapet.domain.dataapi.repositories.PetsRepository
import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.adoptapet.domain.entities.PetEntity
import com.mobdao.adoptapet.domain.entities.SearchFilterEntity
import com.mobdao.local.AnimalLocalDataSource
import com.mobdao.remote.AnimalRemoteDataSource
import com.mobdao.remote.AnimalRemoteDataSource.GeoCoordinates
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetsRepositoryImpl
    @Inject
    constructor(
        private val animalRemoteDataSource: AnimalRemoteDataSource,
        private val animalLocalDataSource: AnimalLocalDataSource,
    ) : PetsRepository {
        override suspend fun getPets(
            pageNumber: Int,
            searchFilter: SearchFilterEntity,
        ): List<PetEntity> {
            val pets: List<PetEntity> =
                animalRemoteDataSource.getPets(
                    pageNumber = pageNumber,
                    locationCoordinates = searchFilter.address.toLocationCoordinates(),
                    petType = searchFilter.petType,
                    petGenders = searchFilter.petGenders,
                )
            saveToDatabase(pets)
            return pets
        }

        override suspend fun getCachedPetById(petId: String): PetEntity? = animalLocalDataSource.getPetById(petId = petId)

        private suspend fun saveToDatabase(pets: List<PetEntity>) {
            animalLocalDataSource.savePets(pets)
        }

        private fun AddressEntity.toLocationCoordinates(): GeoCoordinates =
            GeoCoordinates(
                latitude = latitude,
                longitude = longitude,
            )
    }
