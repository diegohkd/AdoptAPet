package com.mobdao.remote.services

import com.mobdao.remote.responses.AnimalsResponse
import retrofit2.http.*

interface PetFinderService {

    @GET("animals?type=dog&page=1")
    suspend fun getAnimals(): AnimalsResponse
}