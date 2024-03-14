package com.mobdao.domain_api.entitites

data class SearchFilter(
    val coordinates: Coordinates? = null,
    val petType: String? = null,
) {
    data class Coordinates(
        val latitude: Double,
        val longitude: Double,
    )
}