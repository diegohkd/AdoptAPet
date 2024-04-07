package com.mobdao.domain.api.repositories

import com.mobdao.domain.api.entitites.Pet
import com.mobdao.domain.api.entitites.SearchFilter

interface PetsRepository {
    suspend fun getPets(pageNumber: Int, searchFilter: SearchFilter): List<Pet>

    suspend fun getCachedPetById(petId: String): Pet?
}
