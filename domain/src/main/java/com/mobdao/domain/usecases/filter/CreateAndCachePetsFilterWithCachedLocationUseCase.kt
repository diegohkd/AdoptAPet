package com.mobdao.domain.usecases.filter

import com.mobdao.common.exceptions.LocationNotFoundException
import com.mobdao.domain.api.repositories.GeoLocationRepository
import com.mobdao.domain.api.repositories.SearchFilterRepository
import com.mobdao.domain.utils.mappers.SearchFilterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateAndCachePetsFilterWithCachedLocationUseCase @Inject constructor(
    private val geoLocationRepository: GeoLocationRepository,
    private val searchFilterRepository: SearchFilterRepository,
) {

    fun execute(): Flow<Unit> = flow {
        val address = geoLocationRepository.getCachedCurrentLocationAddress()
            ?: throw LocationNotFoundException(
                "Could not save search filter as cached location was not found"
            )
        val searchFilter = SearchFilterEntity(address = address)
        searchFilterRepository.saveSearchFilter(searchFilter)
        emit(Unit)
    }
}