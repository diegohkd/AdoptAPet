package com.mobdao.data

import com.mobdao.cache.database.daos.AnimalDao
import com.mobdao.data.common.AnimalDbEntity
import com.mobdao.data.common.AnimalRemoteResponse
import com.mobdao.data.utils.mappers.AnimalMapper
import com.mobdao.domain_api.PetsRepository
import com.mobdao.domain_api.entitites.Pet
import com.mobdao.domain_api.entitites.SearchFilter
import com.mobdao.remote.services.PetFinderService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetsRepositoryImpl @Inject constructor(
    private val animalDao: AnimalDao,
    private val petFinderService: PetFinderService,
    private val animalMapper: AnimalMapper,
) : PetsRepository {

    override suspend fun getPets(searchFilter: SearchFilter?): List<Pet> {
        val location = searchFilter?.coordinates?.let {
            "${it.latitude},${it.longitude}"
        }
        val animals = petFinderService.getAnimals(
            pageNumber = 1,
            location = location,
            type = searchFilter?.petType,
        ).animals
        saveToDatabase(animals)
        return animalMapper.mapToPet(*animals.toTypedArray())
    }

    override suspend fun getCachedPetById(petId: String): Pet =
        withContext(Dispatchers.IO) {
            animalDao.getById(petId).let { animalMapper.mapToPet(it)  }
        }

    private suspend fun saveToDatabase(animals: List<AnimalRemoteResponse>) {
        val animalsDbEntities: Array<AnimalDbEntity> = animals
            .let(animalMapper::mapToDbEntity)
            .toTypedArray()
        animalDao.insertAll(*animalsDbEntities)
    }
}