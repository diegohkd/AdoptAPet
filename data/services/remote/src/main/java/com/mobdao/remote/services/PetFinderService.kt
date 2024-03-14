package com.mobdao.remote.services

import com.mobdao.remote.responses.AnimalsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PetFinderService {

    @GET("animals")
    suspend fun getAnimals(
        @Query("page")
        pageNumber: Int,
        @Query("location")
        location: String? = null,
        @Query("type")
        type: String? = null,
    ): AnimalsResponse
}