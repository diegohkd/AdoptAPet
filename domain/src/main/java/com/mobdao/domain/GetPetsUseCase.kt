package com.mobdao.domain

import com.mobdao.domain.common_models.Pet
import com.mobdao.domain.common_models.SearchFilter
import com.mobdao.domain.utils.mappers.PetMapper
import com.mobdao.domain.utils.mappers.SearchFilterEntity
import com.mobdao.domain.utils.mappers.SearchFilterMapper
import com.mobdao.domain_api.GeoLocationRepository
import com.mobdao.domain_api.PetsRepository
import com.mobdao.domain_api.entitites.SearchFilter.Coordinates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPetsUseCase @Inject constructor(
    private val petsRepository: PetsRepository,
    private val locationRepository: GeoLocationRepository,
    private val petMapper: PetMapper,
    private val searchFilterMapper: SearchFilterMapper
) {

    fun execute(pageNumber: Int, searchFilter: SearchFilter?): Flow<List<Pet>> = flow {
        emit(
            petsRepository.getPets(
                pageNumber = pageNumber,
                searchFilter = buildSearchFilterEntity(searchFilter)
            )
                .map(petMapper::map)
        )
    }

    // TODO improve this
    private suspend fun buildSearchFilterEntity(searchFilter: SearchFilter?): SearchFilterEntity {
        val address = locationRepository.getCurrentLocationAddress()
        return searchFilter
            ?.let(searchFilterMapper::mapToEntity)
            ?: SearchFilterEntity()
                .let {
                    if (address != null) {
                        it.copy(
                            coordinates = Coordinates(
                                latitude = address.latitude,
                                longitude = address.longitude
                            )
                        )
                    } else {
                        it
                    }
                }
    }
}