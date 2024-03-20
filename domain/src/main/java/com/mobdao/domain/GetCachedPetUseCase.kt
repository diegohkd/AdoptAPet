package com.mobdao.domain

import com.mobdao.domain.common_models.Pet
import com.mobdao.domain.utils.mappers.PetMapper
import com.mobdao.domain_api.repositories.PetsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCachedPetUseCase @Inject constructor(
    private val petsRepository: PetsRepository,
    private val petMapper: PetMapper,
) {

    fun execute(petId: String): Flow<Pet?> = flow {
        emit(petsRepository.getCachedPetById(petId)?.let(petMapper::map))
    }
}