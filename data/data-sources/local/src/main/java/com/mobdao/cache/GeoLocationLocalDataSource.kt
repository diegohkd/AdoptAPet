package com.mobdao.cache

import com.mobdao.cache.database.daos.AddressDao
import com.mobdao.cache.database.entities.Address
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeoLocationLocalDataSource @Inject internal constructor(
    private val addressDao: AddressDao,
) {

    suspend fun saveCurrentAddress(address: Address) {
        withContext(Dispatchers.IO) {
            addressDao.nukeTable()
            addressDao.insertAll(listOf(address))
        }
    }

    suspend fun getCurrentAddress(): Address? =
        withContext(Dispatchers.IO) {
            addressDao.getAll().firstOrNull()
        }
}
