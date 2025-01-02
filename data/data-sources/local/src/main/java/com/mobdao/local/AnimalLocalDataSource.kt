package com.mobdao.local

import com.mobdao.adoptapet.domain.entities.PetEntity
import com.mobdao.local.internal.common.mappers.EntityMapper
import com.mobdao.local.internal.database.daos.AnimalDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalLocalDataSource
    @Inject
    internal constructor(
        private val animalDao: AnimalDao,
        private val entityMapper: EntityMapper,
    ) {
        suspend fun savePets(pets: List<PetEntity>) {
            animalDao.insertAll(entityMapper.toDbModel(pets))
        }

        suspend fun getPetById(petId: String): PetEntity? =
            withContext(Dispatchers.IO) {
                animalDao.getById(petId)?.let(entityMapper::toEntity)
            }
    }
