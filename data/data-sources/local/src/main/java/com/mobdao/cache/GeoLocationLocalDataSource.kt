package com.mobdao.cache

import com.mobdao.cache.models.Address
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeoLocationLocalDataSource @Inject internal constructor() {

    // In-memory caching
    private var currentAddress: Address? = null

    fun saveCurrentAddress(address: Address?) {
        currentAddress = address
    }

    fun getCurrentAddress(): Address? = currentAddress
}