package com.mobdao.data.repositories

import com.mobdao.adoptapet.domain.dataapi.repositories.GeoLocationRepository
import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.adoptapet.domain.entities.GeoCoordinatesEntity
import com.mobdao.local.GeoLocationLocalDataSource
import com.mobdao.remote.GeoLocationRemoteDataSource
import javax.inject.Inject

class GeoLocationRepositoryImpl
    @Inject
    constructor(
        private val geoLocationRemoteDataSource: GeoLocationRemoteDataSource,
        private val geoLocationLocalDataSource: GeoLocationLocalDataSource,
    ) : GeoLocationRepository {
        override suspend fun getCurrentLocationAddress(): AddressEntity? {
            val currentGeoCoordinates: GeoCoordinatesEntity =
                geoLocationRemoteDataSource.getCurrentLocationCoordinates()
            return geoLocationRemoteDataSource
                .getLocationAddress(
                    geoCoordinates = currentGeoCoordinates,
                ).firstOrNull()
        }

        override suspend fun getCachedCurrentLocationAddress(): AddressEntity? = geoLocationLocalDataSource.getCurrentAddress()

        override suspend fun autocompleteLocation(location: String): List<AddressEntity> =
            if (location.isBlank()) {
                emptyList()
            } else {
                geoLocationRemoteDataSource.autocompleteLocation(location)
            }

        override suspend fun cacheCurrentLocationAddress(address: AddressEntity) {
            geoLocationLocalDataSource.saveCurrentAddress(address)
        }
    }
