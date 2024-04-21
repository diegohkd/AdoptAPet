package com.mobdao.domain.dataapi.entitites

// TODO rename to PetsFilter
data class SearchFilter(
    val address: Address,
    val petType: String? = null,
)
