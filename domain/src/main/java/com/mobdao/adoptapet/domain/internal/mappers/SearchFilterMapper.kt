package com.mobdao.adoptapet.domain.internal.mappers

import com.mobdao.adoptapet.domain.internal.AddressEntity
import com.mobdao.adoptapet.domain.internal.SearchFilterEntity
import com.mobdao.adoptapet.domain.models.Address
import com.mobdao.adoptapet.domain.models.SearchFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SearchFilterMapper
    @Inject
    constructor() {
        fun mapToEntity(searchFilter: SearchFilter): SearchFilterEntity =
            SearchFilterEntity(
                address =
                    searchFilter.address.let {
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
                address =
                    searchFilterEntity.address.let {
                        Address(
                            addressLine = it.addressLine,
                            latitude = it.latitude,
                            longitude = it.longitude,
                        )
                    },
                petType = searchFilterEntity.petType,
            )
    }
