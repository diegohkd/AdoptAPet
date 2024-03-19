package com.mobdao.data

import com.mobdao.cache.GeoLocationLocalDataSource
import com.mobdao.data.utils.mappers.AddressMapper
import com.mobdao.domain_api.GeoLocationRepository
import com.mobdao.domain_api.entitites.Address
import com.mobdao.remote.GeoLocationRemoteDataSource
import com.mobdao.remote.responses.GeoCoordinates
import javax.inject.Inject

class GeoLocationRepositoryImpl @Inject constructor(
    private val geoLocationRemoteDataSource: GeoLocationRemoteDataSource,
    private val geoLocationLocalDataSource: GeoLocationLocalDataSource,
    private val addressMapper: AddressMapper,
) : GeoLocationRepository {

    override suspend fun getCurrentLocationAddress(): Address? {
        val currentGeoCoordinates: GeoCoordinates =
            geoLocationRemoteDataSource.getCurrentLocationCoordinates()
        val reverseGeocodeResponse =
            geoLocationRemoteDataSource.getLocationAddress(geoCoordinates = currentGeoCoordinates)
        val address = reverseGeocodeResponse
            .results
            .firstOrNull()
            ?.let(addressMapper::mapToEntity)

        geoLocationLocalDataSource.saveCurrentAddress(
            address = address?.let(addressMapper::mapToCache)
        )
        return address
    }

    override fun getCachedLocationAddress(): Address? =
        geoLocationLocalDataSource.getCurrentAddress()?.let(addressMapper::mapToEntity)

    override suspend fun autocompleteLocation(location: String): List<Address> =
        if (location.isBlank()) {
            emptyList()
        } else {
            geoLocationRemoteDataSource.autocompleteLocation(location)
                .results
                .map(addressMapper::mapToEntity)
        }
}