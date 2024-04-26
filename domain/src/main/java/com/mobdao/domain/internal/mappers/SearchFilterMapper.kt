package com.mobdao.domain.internal.mappers

import com.mobdao.domain.models.Address
import com.mobdao.domain.models.SearchFilter
import com.mobdao.domain.internal.AddressEntity
import com.mobdao.domain.internal.SearchFilterEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SearchFilterMapper @Inject constructor() {

    fun mapToEntity(searchFilter: SearchFilter): SearchFilterEntity =
        SearchFilterEntity(
            address = searchFilter.address.let {
                AddressEntity(
                    addressLine = it.addressLine,
                    latitude = it.latitude,
                    longitude = it.longitude,
                )
            },
            petType = searchFilter.petType,
        )

    fun mapFromEntity(searchFilterEntity: SearchFilterEntity): SearchFilter =
        SearchFilter(
            address = searchFilterEntity.address.let {
                Address(
                    addressLine = it.addressLine,
                    latitude = it.latitude,
                    longitude = it.longitude,
                )
            },
            petType = searchFilterEntity.petType,
        )
}
