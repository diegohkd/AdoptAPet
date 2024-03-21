package com.mobdao.domain

import com.mobdao.common.exceptions.CurrentLocationNotFoundException
import com.mobdao.domain.api.entitites.SearchFilter
import com.mobdao.domain.api.repositories.GeoLocationRepository
import com.mobdao.domain.api.repositories.SearchFilterRepository
import com.mobdao.domain.models.Address
import com.mobdao.domain.utils.mappers.AddressEntity
import com.mobdao.domain.utils.mappers.AddressMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentAddressAndSaveSearchFilterUseCase @Inject constructor(
    private val searchFilterRepository: SearchFilterRepository,
    private val geoLocationRepository: GeoLocationRepository,
    private val addressMapper: AddressMapper,
) {

    fun execute(): Flow<Address> = flow {
        val currentSearchFilter = searchFilterRepository.getSearchFilter()
        // TODO throw a different exception
        val address: AddressEntity = geoLocationRepository.getCurrentLocationAddress()
            ?: throw CurrentLocationNotFoundException("Failed to get current location address")
        val searchFilter: SearchFilter = currentSearchFilter
            ?.copy(address = address)
            ?: SearchFilter(address = address)
        emit(addressMapper.map(address))
        searchFilterRepository.saveSearchFilter(searchFilter)
    }
}
