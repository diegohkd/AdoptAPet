package com.mobdao.adoptapet.domain.entities

// TODO rename to PetsFilter
data class SearchFilterEntity(
    val address: AddressEntity,
    val petType: String? = null,
)
