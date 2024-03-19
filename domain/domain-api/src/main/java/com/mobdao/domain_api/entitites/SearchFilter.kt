package com.mobdao.domain_api.entitites

data class SearchFilter(
    val address: Address,
    val petType: String? = null,
)