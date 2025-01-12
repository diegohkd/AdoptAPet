package com.mobdao.adoptapet.domain.dataapi.repositories

import com.mobdao.adoptapet.domain.entities.PetEntity
import com.mobdao.adoptapet.domain.entities.SearchFilterEntity

interface PetsRepository {
    suspend fun getPets(
        pageNumber: Int,
        searchFilter: SearchFilterEntity,
    ): List<PetEntity>

    suspend fun getCachedPetById(petId: String): PetEntity?
}
