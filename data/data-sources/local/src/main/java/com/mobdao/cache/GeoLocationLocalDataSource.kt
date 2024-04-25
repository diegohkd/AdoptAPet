package com.mobdao.cache

import com.mobdao.cache.common.DomainEntityAddress
import com.mobdao.cache.common.mappers.EntityMapper
import com.mobdao.cache.database.daos.AddressDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeoLocationLocalDataSource @Inject internal constructor(
    private val addressDao: AddressDao,
    private val entityMapper: EntityMapper,
) {

    suspend fun saveCurrentAddress(address: DomainEntityAddress) {
        withContext(Dispatchers.IO) {
            addressDao.nukeTable()
            addressDao.insertAll(listOf(entityMapper.toDbEntity(address)))
        }
    }

    suspend fun getCurrentAddress(): DomainEntityAddress? =
        withContext(Dispatchers.IO) {
            addressDao.getAll().firstOrNull()?.let(entityMapper::toDomainEntity)
        }
}
