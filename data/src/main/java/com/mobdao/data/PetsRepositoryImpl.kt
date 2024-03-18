package com.mobdao.data

import com.mobdao.cache.AnimalLocalDataSource
import com.mobdao.data.common.AnimalDbEntity
import com.mobdao.data.common.AnimalRemoteResponse
import com.mobdao.data.utils.mappers.AnimalMapper
import com.mobdao.domain_api.PetsRepository
import com.mobdao.domain_api.entitites.Pet
import com.mobdao.domain_api.entitites.SearchFilter
import com.mobdao.remote.AnimalRemoteDataSource
import com.mobdao.remote.responses.Animal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetsRepositoryImpl @Inject constructor(
    private val animalRemoteDataSource: AnimalRemoteDataSource,
    private val animalLocalDataSource: AnimalLocalDataSource,
    private val animalMapper: AnimalMapper,
) : PetsRepository {

    override suspend fun getPets(pageNumber: Int, searchFilter: SearchFilter?): List<Pet> {
        val location = searchFilter?.coordinates?.let {
            "${it.latitude},${it.longitude}"
        }

        val animals: List<Animal> = animalRemoteDataSource.getAnimals(
            pageNumber = pageNumber,
            formattedLocationCoordinates = location,
            animalType = searchFilter?.petType
        )
        saveToDatabase(animals)
        return animalMapper.mapToPet(*animals.toTypedArray())
    }

    override suspend fun getCachedPetById(petId: String): Pet? =
        animalLocalDataSource.getAnimalById(animalId = petId)?.let { animalMapper.mapToPet(it) }

    private suspend fun saveToDatabase(animals: List<AnimalRemoteResponse>) {
        val animalsDbEntities: List<AnimalDbEntity> = animals.let(animalMapper::mapToDbEntity)
        animalLocalDataSource.saveAnimals(animalsDbEntities)
    }
}