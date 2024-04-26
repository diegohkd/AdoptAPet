package com.mobdao.local

import com.mobdao.local.internal.common.mappers.EntityMapper
import com.mobdao.local.internal.database.daos.AnimalDao
import com.mobdao.domain.entities.Pet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalLocalDataSource @Inject internal constructor(
    private val animalDao: AnimalDao,
    private val entityMapper: EntityMapper,
) {

    suspend fun savePets(pets: List<Pet>) {
        animalDao.insertAll(entityMapper.toAnimal(pets))
    }

    suspend fun getPetById(petId: String): Pet? =
        withContext(Dispatchers.IO) {
            animalDao.getById(petId)?.let(entityMapper::toPet)
        }
}
