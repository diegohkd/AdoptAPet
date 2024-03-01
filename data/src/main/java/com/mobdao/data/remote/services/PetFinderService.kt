package com.mobdao.data.remote.services

import com.mobdao.data.remote.responses.AnimalsResponse
import retrofit2.http.*

interface PetFinderService {

    @GET("animals?type=dog&page=1")
    suspend fun getAnimals(): AnimalsResponse
}