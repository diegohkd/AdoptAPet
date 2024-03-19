package com.mobdao.remote.services

import com.mobdao.remote.responses.ReverseGeocodeResponse
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
    ): ReverseGeocodeResponse
}