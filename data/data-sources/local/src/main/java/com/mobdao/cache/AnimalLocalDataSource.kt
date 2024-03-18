package com.mobdao.cache

import com.mobdao.cache.database.daos.AnimalDao
import com.mobdao.cache.database.entities.Animal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalLocalDataSource @Inject internal constructor(private val animalDao: AnimalDao) {

    suspend fun saveAnimals(animals: List<Animal>) {
        animalDao.insertAll(*animals.toTypedArray())
    }

    suspend fun getAnimalById(animalId: String): Animal? =
        withContext(Dispatchers.IO) {
            animalDao.getById(animalId)
        }
}