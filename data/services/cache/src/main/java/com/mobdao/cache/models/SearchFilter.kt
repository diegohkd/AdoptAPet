package com.mobdao.cache.models

data class SearchFilter(
    val coordinates: Coordinates?,
    val petType: String?,
) {
    data class Coordinates(
        val latitude: Double,
        val longitude: Double,
    )
}
