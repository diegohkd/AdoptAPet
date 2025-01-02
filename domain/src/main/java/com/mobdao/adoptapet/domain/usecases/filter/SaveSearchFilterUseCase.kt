package com.mobdao.adoptapet.domain.usecases.filter

import com.mobdao.adoptapet.domain.dataapi.repositories.GeoLocationRepository
import com.mobdao.adoptapet.domain.dataapi.repositories.SearchFilterRepository
import com.mobdao.adoptapet.domain.internal.mappers.AddressMapper
import com.mobdao.adoptapet.domain.internal.mappers.SearchFilterMapper
import com.mobdao.adoptapet.domain.models.SearchFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveSearchFilterUseCase
    @Inject
    internal constructor(
        private val searchFilterRepository: SearchFilterRepository,
        private val geoLocationRepository: GeoLocationRepository,
        private val addressMapper: AddressMapper,
        private val searchFilterMapper: SearchFilterMapper,
    ) {
        fun execute(filter: SearchFilter): Flow<Unit> =
            flow {
                geoLocationRepository.cacheCurrentLocationAddress(addressMapper.map(filter.address))
                searchFilterRepository.saveSearchFilter(searchFilterMapper.mapToEntity(filter))
                emit(Unit)
            }
    }
