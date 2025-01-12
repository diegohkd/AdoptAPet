package com.mobdao.adoptapet.domain.internal.mappers

import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.adoptapet.domain.models.Address
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AddressMapper
    @Inject
    constructor() {
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
