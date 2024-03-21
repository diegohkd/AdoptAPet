package com.mobdao.domain

import com.mobdao.domain.api.repositories.GeoLocationRepository
import com.mobdao.domain.models.Address
import com.mobdao.domain.utils.mappers.AddressMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCachedCurrentAddressUseCase @Inject constructor(
    private val locationRepository: GeoLocationRepository,
    private val addressMapper: AddressMapper,
) {

    fun execute(): Flow<Address?> = flow {
        emit(locationRepository.getCachedLocationAddress()?.let(addressMapper::map))
    }
}
