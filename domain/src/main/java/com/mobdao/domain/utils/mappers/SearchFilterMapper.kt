package com.mobdao.domain.utils.mappers

import com.mobdao.domain.common_models.SearchFilter
import com.mobdao.domain_api.entitites.SearchFilter.Coordinates
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchFilterMapper @Inject constructor() {

    fun mapToEntity(searchFilter: SearchFilter): SearchFilterEntity =
        SearchFilterEntity(
            coordinates = searchFilter.coordinates?.let {
                Coordinates(
                    latitude = it.latitude,
                    longitude = it.longitude,
                )
            },
            petType = searchFilter.petType,
        )

    fun mapFromEntity(searchFilterEntity: SearchFilterEntity): SearchFilter =
        SearchFilter(
            coordinates = searchFilterEntity.coordinates?.let {
                SearchFilter.Coordinates(
                    latitude = it.latitude,
                    longitude = it.longitude,
                )
            },
            petType = searchFilterEntity.petType,
        )
}