package com.mobdao.domain_api

import com.mobdao.domain_api.entitites.Pet

interface PetsRepository {
    suspend fun getPets(): List<Pet>

    suspend fun getCachedPetById(petId: String): Pet
}