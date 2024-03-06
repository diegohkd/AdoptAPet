package com.mobdao.data

import com.mobdao.data.utils.mappers.AddressMapper
import com.mobdao.domain_api.GeoLocationRepository
import com.mobdao.domain_api.entitites.Address
import com.mobdao.remote.GetCurrentLocationAddressDataSource
import javax.inject.Inject

class GeoLocationRepositoryImpl @Inject constructor(
    private val getCurrentLocationAddressDataSource: GetCurrentLocationAddressDataSource,
    private val addressMapper: AddressMapper,
) : GeoLocationRepository {

    override suspend fun getCurrentLocationAddress(): Address? =
        getCurrentLocationAddressDataSource.getCurrentLocation()?.let(addressMapper::mapToEntity)
}