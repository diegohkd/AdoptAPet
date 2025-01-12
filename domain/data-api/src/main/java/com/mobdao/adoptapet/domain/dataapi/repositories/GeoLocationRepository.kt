package com.mobdao.adoptapet.domain.dataapi.repositories

import com.mobdao.adoptapet.domain.entities.AddressEntity

interface GeoLocationRepository {
    suspend fun getCurrentLocationAddress(): AddressEntity?

    suspend fun getCachedCurrentLocationAddress(): AddressEntity?

    suspend fun cacheCurrentLocationAddress(address: AddressEntity)

    suspend fun autocompleteLocation(location: String): List<AddressEntity>
}
