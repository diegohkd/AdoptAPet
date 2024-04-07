package com.mobdao.domain.utils.mappers

import com.mobdao.domain.models.Address
import com.mobdao.domain.utils.AddressEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressMapper @Inject constructor() {

    fun map(addressEntity: AddressEntity): Address =
        with(addressEntity) {
            Address(
                addressLine = addressLine,
                latitude = latitude,
                longitude = longitude,
            )
        }

    fun map(address: Address): AddressEntity =
        with(address) {
            AddressEntity(
                addressLine = addressLine,
                latitude = latitude,
                longitude = longitude,
            )
        }
}
