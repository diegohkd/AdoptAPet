package com.mobdao.domain.dataapi.repositories

import com.mobdao.domain.dataapi.entitites.Pet
import com.mobdao.domain.dataapi.entitites.SearchFilter

interface PetsRepository {
    suspend fun getPets(pageNumber: Int, searchFilter: SearchFilter): List<Pet>

    suspend fun getCachedPetById(petId: String): Pet?
}
