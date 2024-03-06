package com.mobdao.data.utils.mappers

import com.mobdao.data.common.AddressRemoteResponse
import com.mobdao.domain_api.entitites.Address
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressMapper @Inject constructor() {

    fun mapToEntity(address: AddressRemoteResponse): Address =
        with(address) {
            Address(
                addressLine = addressLine,
                latitude = latitude,
                longitude = longitude,
            )
        }
}