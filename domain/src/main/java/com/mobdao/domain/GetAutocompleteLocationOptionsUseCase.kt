package com.mobdao.domain

import com.mobdao.domain.common_models.Address
import com.mobdao.domain.utils.mappers.AddressMapper
import com.mobdao.domain_api.repositories.GeoLocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAutocompleteLocationOptionsUseCase @Inject constructor(
    private val geoLocationRepository: GeoLocationRepository,
    private val addressMapper: AddressMapper,
) {

    fun execute(location: String): Flow<List<Address>> = flow {
        emit(geoLocationRepository.autocompleteLocation(location).map(addressMapper::map))
    }
}