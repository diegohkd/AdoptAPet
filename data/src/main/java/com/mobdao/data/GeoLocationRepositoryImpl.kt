package com.mobdao.data

import com.mobdao.cache.GeoLocationLocalDataSource
import com.mobdao.data.utils.mappers.AddressMapper
import com.mobdao.domain_api.GeoLocationRepository
import com.mobdao.domain_api.entitites.Address
import com.mobdao.remote.GeoLocationDataSource
import javax.inject.Inject

class GeoLocationRepositoryImpl @Inject constructor(
    private val geoLocationDataSource: GeoLocationDataSource,
    private val addressMapper: AddressMapper,
    private val geoLocationLocalDataSource: GeoLocationLocalDataSource,
) : GeoLocationRepository {

    override suspend fun getCurrentLocationAddress(): Address? {
        val address = geoLocationDataSource.getCurrentAddress()
        geoLocationLocalDataSource.saveCurrentAddress(address = address?.let(addressMapper::mapToCache))
        return address?.let(addressMapper::mapToEntity)
    }

    override fun getCachedLocationAddress(): Address? =
        geoLocationLocalDataSource.getCurrentAddress()?.let(addressMapper::mapToEntity)
}