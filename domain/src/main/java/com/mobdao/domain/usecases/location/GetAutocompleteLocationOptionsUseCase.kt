package com.mobdao.domain.usecases.location

import com.mobdao.domain.api.repositories.GeoLocationRepository
import com.mobdao.domain.models.Address
import com.mobdao.domain.utils.mappers.AddressMapper
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
