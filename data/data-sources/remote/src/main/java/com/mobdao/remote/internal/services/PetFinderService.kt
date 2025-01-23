package com.mobdao.remote.internal.services

import com.mobdao.remote.internal.responses.AnimalsResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface PetFinderService {
    @GET("animals")
    suspend fun getAnimals(
        @Query("page")
        pageNumber: Int,
        @Query("location")
        location: String? = null,
        @Query("type")
        type: String? = null,
        @Query("gender")
        gender: String? = null,
    ): AnimalsResponse
}
