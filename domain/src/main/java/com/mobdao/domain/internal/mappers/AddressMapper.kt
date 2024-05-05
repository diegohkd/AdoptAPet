package com.mobdao.domain.internal.mappers

import com.mobdao.domain.internal.AddressEntity
import com.mobdao.domain.models.Address
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AddressMapper @Inject constructor() {

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
