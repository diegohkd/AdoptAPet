package com.mobdao.data.repositories

import com.mobdao.adoptapet.domain.entities.Address
import com.mobdao.adoptapet.domain.entities.Pet
import com.mobdao.adoptapet.domain.entities.SearchFilter
import com.mobdao.domain.dataapi.repositories.PetsRepository
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
            searchFilter: SearchFilter,
        ): List<Pet> {
            val pets: List<Pet> =
                animalRemoteDataSource.getPets(
                    pageNumber = pageNumber,
                    locationCoordinates = searchFilter.address.toLocationCoordinates(),
                    animalType = searchFilter.petType,
                )
            saveToDatabase(pets)
            return pets
        }

        override suspend fun getCachedPetById(petId: String): Pet? = animalLocalDataSource.getPetById(petId = petId)

        private suspend fun saveToDatabase(pets: List<Pet>) {
            animalLocalDataSource.savePets(pets)
        }

        private fun Address.toLocationCoordinates(): GeoCoordinates =
            GeoCoordinates(
                latitude = latitude,
                longitude = longitude,
            )
    }
