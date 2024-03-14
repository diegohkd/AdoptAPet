package com.mobdao.data.utils.mappers

import com.mobdao.cache.models.SearchFilter.Coordinates
import com.mobdao.data.common.SearchFilterCache
import com.mobdao.domain_api.entitites.SearchFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchFilterMapper @Inject constructor() {

    fun mapToCacheModel(searchFilter: SearchFilter): SearchFilterCache =
        SearchFilterCache(
            coordinates = searchFilter.coordinates?.let {
                Coordinates(
                    latitude = it.latitude,
                    longitude = it.longitude,
                )
            },
            petType = searchFilter.petType
        )

    fun mapFromCacheModel(searchFilterCache: SearchFilterCache): SearchFilter =
        SearchFilter(
            coordinates = searchFilterCache.coordinates?.let {
                SearchFilter.Coordinates(
                    latitude = it.latitude,
                    longitude = it.longitude,
                )
            },
            petType = searchFilterCache.petType
        )
}