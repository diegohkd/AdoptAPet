package com.mobdao.data

import com.mobdao.data.cache.database.daos.AnimalDao
import com.mobdao.data.common.AnimalDbEntity
import com.mobdao.data.common.AnimalRemoteResponse
import com.mobdao.data.remote.services.PetFinderService
import com.mobdao.data.utils.AnimalMapper
import com.mobdao.domain_api.PetsRepository
import com.mobdao.domain_api.entitites.Pet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetsRepositoryImpl @Inject constructor(
    private val animalDao: AnimalDao,
    private val petFinderService: PetFinderService,
    private val animalMapper: AnimalMapper,
) : PetsRepository {

    override suspend fun getPets(): List<Pet> {
        val animals = petFinderService.getAnimals().animals
        saveToDatabase(animals)
        return animalMapper.mapToPet(*animals.toTypedArray())
    }

    private suspend fun saveToDatabase(animals: List<AnimalRemoteResponse>) {
        val animalsDbEntities: Array<AnimalDbEntity> = animals
            .let(animalMapper::mapToDbEntity)
            .toTypedArray()
        animalDao.insertAll(*animalsDbEntities)
    }
}