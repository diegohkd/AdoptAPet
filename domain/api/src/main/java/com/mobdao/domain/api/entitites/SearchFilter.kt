package com.mobdao.domain.api.entitites

// TODO rename to PetsFilter
data class SearchFilter(
    val address: Address,
    val petType: String? = null,
)
