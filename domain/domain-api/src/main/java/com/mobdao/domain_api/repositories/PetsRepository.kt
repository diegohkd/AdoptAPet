package com.mobdao.domain_api.repositories

import com.mobdao.domain_api.entitites.Pet
import com.mobdao.domain_api.entitites.SearchFilter

interface PetsRepository {
    suspend fun getPets(pageNumber: Int, searchFilter: SearchFilter?): List<Pet>

    suspend fun getCachedPetById(petId: String): Pet?
}