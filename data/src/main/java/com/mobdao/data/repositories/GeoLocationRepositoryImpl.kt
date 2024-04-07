package com.mobdao.data.repositories

import com.mobdao.cache.GeoLocationLocalDataSource
import com.mobdao.data.utils.mappers.AddressMapper
import com.mobdao.domain.api.entitites.Address
import com.mobdao.domain.api.repositories.GeoLocationRepository
import com.mobdao.remote.GeoLocationRemoteDataSource
import com.mobdao.remote.responses.GeoCoordinates
import com.mobdao.remote.responses.GeocodeResponse
import javax.inject.Inject

class GeoLocationRepositoryImpl @Inject constructor(
    private val geoLocationRemoteDataSource: GeoLocationRemoteDataSource,
    private val geoLocationLocalDataSource: GeoLocationLocalDataSource,
    private val addressMapper: AddressMapper,
) : GeoLocationRepository {

    override suspend fun getCurrentLocationAddress(): Address? {
        val currentGeoCoordinates: GeoCoordinates =
            geoLocationRemoteDataSource.getCurrentLocationCoordinates()
        val reverseGeocodeResponse: GeocodeResponse =
            geoLocationRemoteDataSource.getLocationAddress(geoCoordinates = currentGeoCoordinates)
        return reverseGeocodeResponse
            .results
            .firstOrNull()
            ?.let(addressMapper::mapToEntity)
    }

    override suspend fun getCachedCurrentLocationAddress(): Address? =
        geoLocationLocalDataSource.getCurrentAddress()?.let(addressMapper::mapToEntity)

    override suspend fun autocompleteLocation(location: String): List<Address> =
        if (location.isBlank()) {
            emptyList()
        } else {
            geoLocationRemoteDataSource.autocompleteLocation(location)
                .results
                .map(addressMapper::mapToEntity)
        }

    override suspend fun cacheCurrentLocationAddress(address: Address) {
        geoLocationLocalDataSource.saveCurrentAddress(addressMapper.mapToDbEntity(address))
    }
}
