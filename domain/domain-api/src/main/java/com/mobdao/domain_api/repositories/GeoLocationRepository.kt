package com.mobdao.domain_api.repositories

import com.mobdao.domain_api.entitites.Address

interface GeoLocationRepository {
    suspend fun getCurrentLocationAddress(): Address?
    fun getCachedLocationAddress(): Address?
    suspend fun autocompleteLocation(location: String): List<Address>
}