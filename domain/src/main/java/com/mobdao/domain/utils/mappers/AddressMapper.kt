package com.mobdao.domain.utils.mappers

import com.mobdao.domain.common_models.Address
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
}