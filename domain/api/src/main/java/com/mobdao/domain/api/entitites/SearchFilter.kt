package com.mobdao.domain.api.entitites

data class SearchFilter(
    val address: Address,
    val petType: String? = null,
)
