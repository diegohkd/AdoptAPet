package com.mobdao.domain.dataapi.repositories

import com.mobdao.adoptapet.domain.entities.Address

interface GeoLocationRepository {
    suspend fun getCurrentLocationAddress(): Address?

    suspend fun getCachedCurrentLocationAddress(): Address?

    suspend fun cacheCurrentLocationAddress(address: Address)

    suspend fun autocompleteLocation(location: String): List<Address>
}
