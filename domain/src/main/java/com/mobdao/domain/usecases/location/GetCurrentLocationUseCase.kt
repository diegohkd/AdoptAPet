package com.mobdao.domain.usecases.location

import com.mobdao.common.exceptions.CurrentLocationNotFoundException
import com.mobdao.domain.api.repositories.GeoLocationRepository
import com.mobdao.domain.models.Address
import com.mobdao.domain.utils.AddressEntity
import com.mobdao.domain.utils.mappers.AddressMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val geoLocationRepository: GeoLocationRepository,
    private val addressMapper: AddressMapper,
) {

    fun execute(): Flow<Address> = flow {
        val address: AddressEntity = geoLocationRepository.getCurrentLocationAddress()
            ?: throw CurrentLocationNotFoundException("Failed to get current location address")

        emit(addressMapper.map(address))
    }
}