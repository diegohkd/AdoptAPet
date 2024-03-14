package com.mobdao.domain

import com.mobdao.domain.common_models.Address
import com.mobdao.domain.utils.mappers.AddressMapper
import com.mobdao.domain_api.GeoLocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentAddressUseCase @Inject constructor(
    private val geoLocationRepository: GeoLocationRepository,
    private val addressMapper: AddressMapper,
) {

    fun execute(): Flow<Address?> = flow {
        emit(geoLocationRepository.getCurrentLocationAddress()?.let(addressMapper::map))
    }
}