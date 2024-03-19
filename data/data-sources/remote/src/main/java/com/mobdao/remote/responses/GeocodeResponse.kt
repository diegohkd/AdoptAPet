package com.mobdao.remote.responses

data class GeocodeResponse(
    val results: List<GeocodeResult>,
)

data class GeocodeResult(
    val formatted: String,
    val lat: Double,
    val lon: Double,
)