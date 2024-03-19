package com.mobdao.data.utils.mappers

import com.mobdao.data.common.AddressCache
import com.mobdao.domain_api.entitites.Address
import com.mobdao.remote.responses.GeocodeResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressMapper @Inject constructor() {

    fun mapToEntity(address: AddressCache): Address =
        with(address) {
            Address(
                addressLine = addressLine,
                latitude = latitude,
                longitude = longitude,
            )
        }

    fun mapToEntity(geocodeResult: GeocodeResult): Address =
        with(geocodeResult) {
            Address(
                addressLine = formatted,
                latitude = lat,
                longitude = lon,
            )
        }

    fun mapToCache(address: Address): AddressCache =
        with(address) {
            AddressCache(
                addressLine = addressLine,
                latitude = latitude,
                longitude = longitude,
            )
        }
}