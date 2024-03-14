package com.mobdao.data

import com.mobdao.cache.CurrentAddressHolder
import com.mobdao.data.utils.mappers.AddressMapper
import com.mobdao.domain_api.GeoLocationRepository
import com.mobdao.domain_api.entitites.Address
import com.mobdao.remote.GetCurrentLocationAddressDataSource
import javax.inject.Inject

class GeoLocationRepositoryImpl @Inject constructor(
    private val getCurrentLocationAddressDataSource: GetCurrentLocationAddressDataSource,
    private val addressMapper: AddressMapper,
    private val currentAddressHolder: CurrentAddressHolder,
) : GeoLocationRepository {

    override suspend fun getCurrentLocationAddress(): Address? {
        val address = getCurrentLocationAddressDataSource.getCurrentLocation()
        currentAddressHolder.set(address = address?.let(addressMapper::mapToCache))
        return address?.let(addressMapper::mapToEntity)
    }

    override fun getCachedLocationAddress(): Address? =
        currentAddressHolder.get()?.let(addressMapper::mapToEntity)
}