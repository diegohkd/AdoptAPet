package com.mobdao.remote.internal.services

import com.mobdao.remote.internal.responses.GeocodeResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface GeoapifyService {
    @GET("geocode/reverse")
    suspend fun reverseGeocode(
        @Query("apiKey")
        apiKey: String,
        @Query("lat")
        latitude: Double,
        @Query("lon")
        longitude: Double,
        @Query("format")
        format: String? = null,
    ): GeocodeResponse

    @GET("geocode/autocomplete")
    suspend fun autocomplete(
        @Query("apiKey")
        apiKey: String,
        @Query("text")
        text: String,
        @Query("format")
        format: String? = null,
    ): GeocodeResponse
}
