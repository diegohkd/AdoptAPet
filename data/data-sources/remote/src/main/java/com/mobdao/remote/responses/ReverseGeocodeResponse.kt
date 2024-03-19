package com.mobdao.remote.responses

data class ReverseGeocodeResponse(
    val results: List<ReverseGeocodeResult>,
)

data class ReverseGeocodeResult(
    val formatted: String,
    val lat: Double,
    val lon: Double,
)