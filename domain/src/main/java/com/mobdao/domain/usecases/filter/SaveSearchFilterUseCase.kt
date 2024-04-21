package com.mobdao.domain.usecases.filter

import com.mobdao.domain.dataapi.repositories.GeoLocationRepository
import com.mobdao.domain.dataapi.repositories.SearchFilterRepository
import com.mobdao.domain.models.SearchFilter
import com.mobdao.domain.utils.mappers.AddressMapper
import com.mobdao.domain.utils.mappers.SearchFilterMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveSearchFilterUseCase @Inject constructor(
    private val searchFilterRepository: SearchFilterRepository,
    private val geoLocationRepository: GeoLocationRepository,
    private val addressMapper: AddressMapper,
    private val searchFilterMapper: SearchFilterMapper,
) {

    fun execute(filter: SearchFilter): Flow<Unit> = flow {
        geoLocationRepository.cacheCurrentLocationAddress(addressMapper.map(filter.address))
        searchFilterRepository.saveSearchFilter(searchFilterMapper.mapToEntity(filter))
        emit(Unit)
    }
}
