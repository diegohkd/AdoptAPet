package com.mobdao.domain_api

import com.mobdao.domain_api.entitites.Address

interface GeoLocationRepository {
    suspend fun getCurrentLocationAddress(): Address?
}