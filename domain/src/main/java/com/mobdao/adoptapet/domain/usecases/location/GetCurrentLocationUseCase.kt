package com.mobdao.adoptapet.domain.usecases.location

import com.mobdao.adoptapet.common.exceptions.CurrentLocationNotFoundException
import com.mobdao.adoptapet.domain.dataapi.repositories.GeoLocationRepository
import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.adoptapet.domain.internal.mappers.AddressMapper
import com.mobdao.adoptapet.domain.models.Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentLocationUseCase
    @Inject
    internal constructor(
        private val geoLocationRepository: GeoLocationRepository,
        private val addressMapper: AddressMapper,
    ) {
        fun execute(): Flow<Address> =
            flow {
                val address: AddressEntity =
                    geoLocationRepository.getCurrentLocationAddress()
                        ?: throw CurrentLocationNotFoundException("Failed to get current location address")

                emit(addressMapper.map(address))
            }
    }
