package com.mobdao.domain

import com.mobdao.domain.common_models.Address
import com.mobdao.domain.utils.mappers.AddressEntity
import com.mobdao.domain.utils.mappers.AddressMapper
import com.mobdao.domain_api.GeoLocationRepository
import com.mobdao.domain_api.SearchFilterRepository
import com.mobdao.domain_api.entitites.SearchFilter
import com.mobdao.domain_api.entitites.SearchFilter.Coordinates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentAddressAndSaveSearchFilterUseCase @Inject constructor(
    private val searchFilterRepository: SearchFilterRepository,
    private val geoLocationRepository: GeoLocationRepository,
    private val addressMapper: AddressMapper,
) {

    fun execute(): Flow<Address> = flow {
        // TODO throw a different exception
        val address: AddressEntity =
            geoLocationRepository.getCurrentLocationAddress() ?: throw Exception()
        val searchFilter = SearchFilter(
            coordinates = Coordinates(
                latitude = address.latitude,
                longitude = address.longitude
            )
        )
        emit(addressMapper.map(address))
        searchFilterRepository.saveSearchFilter(searchFilter)
    }
}