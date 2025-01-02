package com.mobdao.domain.usecases.location

import com.mobdao.adoptapet.domain.dataapi.repositories.GeoLocationRepository
import com.mobdao.domain.internal.mappers.AddressMapper
import com.mobdao.domain.models.Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAutocompleteLocationOptionsUseCase
    @Inject
    internal constructor(
        private val geoLocationRepository: GeoLocationRepository,
        private val addressMapper: AddressMapper,
    ) {
        fun execute(location: String): Flow<List<Address>> =
            flow {
                emit(geoLocationRepository.autocompleteLocation(location).map(addressMapper::map))
            }
    }
