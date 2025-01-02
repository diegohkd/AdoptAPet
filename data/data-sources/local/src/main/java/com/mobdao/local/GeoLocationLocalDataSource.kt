package com.mobdao.local

import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.local.internal.common.mappers.EntityMapper
import com.mobdao.local.internal.database.daos.AddressDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeoLocationLocalDataSource
    @Inject
    internal constructor(
        private val addressDao: AddressDao,
        private val entityMapper: EntityMapper,
    ) {
        suspend fun saveCurrentAddress(address: AddressEntity) {
            withContext(Dispatchers.IO) {
                addressDao.nukeTable()
                addressDao.insertAll(listOf(entityMapper.toDbModel(address)))
            }
        }

        suspend fun getCurrentAddress(): AddressEntity? =
            withContext(Dispatchers.IO) {
                addressDao.getAll().firstOrNull()?.let(entityMapper::toEntity)
            }
    }
