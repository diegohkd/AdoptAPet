package com.mobdao.domain.entities

// TODO rename to PetsFilter
data class SearchFilter(
    val address: Address,
    val petType: String? = null,
)
