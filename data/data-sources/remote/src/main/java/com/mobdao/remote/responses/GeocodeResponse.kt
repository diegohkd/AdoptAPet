package com.mobdao.remote.responses

internal data class GeocodeResponse(
    val results: List<GeocodeResult>,
)

internal data class GeocodeResult(
    val formatted: String,
    val lat: Double,
    val lon: Double,
)
