package com.mobdao.data.utils.mappers

import com.mobdao.data.common.AddressCache
import com.mobdao.data.common.SearchFilterCache
import com.mobdao.domain.dataapi.entitites.Address
import com.mobdao.domain.dataapi.entitites.SearchFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchFilterMapper @Inject constructor() {

    fun mapToCacheModel(searchFilter: SearchFilter): SearchFilterCache =
        SearchFilterCache(
            address = searchFilter.address.let {
                AddressCache(
                    addressLine = it.addressLine,
                    latitude = it.latitude,
                    longitude = it.longitude,
                )
            },
            petType = searchFilter.petType
        )

    fun mapFromCacheModel(searchFilterCache: SearchFilterCache): SearchFilter =
        SearchFilter(
            address = searchFilterCache.address.let {
                Address(
                    addressLine = it.addressLine,
                    latitude = it.latitude,
                    longitude = it.longitude,
                )
            },
            petType = searchFilterCache.petType
        )
}
