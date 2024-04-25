package com.mobdao.domain.dataapi.repositories

import com.mobdao.domain.entities.Address

interface GeoLocationRepository {
    suspend fun getCurrentLocationAddress(): Address?
    suspend fun getCachedCurrentLocationAddress(): Address?
    suspend fun cacheCurrentLocationAddress(address: Address)
    suspend fun autocompleteLocation(location: String): List<Address>
}
