package com.mobdao.domain.dataapi.repositories

import com.mobdao.adoptapet.domain.entities.Pet
import com.mobdao.adoptapet.domain.entities.SearchFilter

interface PetsRepository {
    suspend fun getPets(
        pageNumber: Int,
        searchFilter: SearchFilter,
    ): List<Pet>

    suspend fun getCachedPetById(petId: String): Pet?
}
