package com.mobdao.domain_api.repositories

import com.mobdao.domain_api.entitites.Address

interface GeoLocationRepository {
    suspend fun getCurrentLocationAddress(): Address?
    // TODO maybe improve this. It might be better to return a Flow
    fun getCachedLocationAddress(): Address?

    suspend fun autocompleteLocation(location: String): List<Address>
}