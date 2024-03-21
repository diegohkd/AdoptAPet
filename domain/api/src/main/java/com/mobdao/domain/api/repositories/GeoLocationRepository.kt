package com.mobdao.domain.api.repositories

import com.mobdao.domain.api.entitites.Address

interface GeoLocationRepository {
    suspend fun getCurrentLocationAddress(): Address?
    fun getCachedLocationAddress(): Address?
    suspend fun autocompleteLocation(location: String): List<Address>
}
