package com.mobdao.data.utils.mappers

import com.mobdao.data.common.AddressDbEntity
import com.mobdao.domain.api.entitites.Address
import com.mobdao.remote.responses.GeocodeResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressMapper @Inject constructor() {

    fun mapToEntity(address: AddressDbEntity): Address =
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

    fun mapToDbEntity(address: Address): AddressDbEntity =
        with(address) {
            AddressDbEntity(
                latitude = latitude,
                longitude = longitude,
                addressLine = addressLine,
            )
        }
}
