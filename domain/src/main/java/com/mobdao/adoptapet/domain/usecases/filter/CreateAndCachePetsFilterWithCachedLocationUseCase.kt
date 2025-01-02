package com.mobdao.adoptapet.domain.usecases.filter

import com.mobdao.adoptapet.common.exceptions.LocationNotFoundException
import com.mobdao.adoptapet.domain.dataapi.repositories.GeoLocationRepository
import com.mobdao.adoptapet.domain.dataapi.repositories.SearchFilterRepository
import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.adoptapet.domain.entities.SearchFilterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateAndCachePetsFilterWithCachedLocationUseCase
    @Inject
    constructor(
        private val geoLocationRepository: GeoLocationRepository,
        private val searchFilterRepository: SearchFilterRepository,
    ) {
        fun execute(): Flow<Unit> =
            flow {
                val address: AddressEntity =
                    geoLocationRepository.getCachedCurrentLocationAddress()
                        ?: throw LocationNotFoundException(
                            "Could not save search filter as cached location was not found",
                        )
                val searchFilter: SearchFilterEntity = SearchFilterEntity(address = address)
                searchFilterRepository.saveSearchFilter(searchFilter)
                emit(Unit)
            }
    }
