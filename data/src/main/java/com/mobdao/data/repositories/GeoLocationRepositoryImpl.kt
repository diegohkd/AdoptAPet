package com.mobdao.data.repositories

import com.mobdao.cache.GeoLocationLocalDataSource
import com.mobdao.domain.dataapi.repositories.GeoLocationRepository
import com.mobdao.domain.entities.Address
import com.mobdao.domain.entities.GeoCoordinates
import com.mobdao.remote.GeoLocationRemoteDataSource
import javax.inject.Inject

class GeoLocationRepositoryImpl @Inject constructor(
    private val geoLocationRemoteDataSource: GeoLocationRemoteDataSource,
    private val geoLocationLocalDataSource: GeoLocationLocalDataSource,
) : GeoLocationRepository {

    override suspend fun getCurrentLocationAddress(): Address? {
        val currentGeoCoordinates: GeoCoordinates =
            geoLocationRemoteDataSource.getCurrentLocationCoordinates()
        return geoLocationRemoteDataSource.getLocationAddress(
            geoCoordinates = currentGeoCoordinates
        ).firstOrNull()
    }

    override suspend fun getCachedCurrentLocationAddress(): Address? =
        geoLocationLocalDataSource.getCurrentAddress()

    override suspend fun autocompleteLocation(location: String): List<Address> =
        if (location.isBlank()) {
            emptyList()
        } else {
            geoLocationRemoteDataSource.autocompleteLocation(location)
        }

    override suspend fun cacheCurrentLocationAddress(address: Address) {
        geoLocationLocalDataSource.saveCurrentAddress(address)
    }
}
