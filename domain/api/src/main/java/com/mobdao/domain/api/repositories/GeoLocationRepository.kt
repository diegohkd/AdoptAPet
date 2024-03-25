package com.mobdao.domain.api.repositories

import com.mobdao.domain.api.entitites.Address

interface GeoLocationRepository {
    suspend fun getCurrentLocationAddress(): Address?
    suspend fun getCachedCurrentLocationAddress(): Address?
    suspend fun cacheCurrentLocationAddress(address: Address)
    suspend fun autocompleteLocation(location: String): List<Address>
}
