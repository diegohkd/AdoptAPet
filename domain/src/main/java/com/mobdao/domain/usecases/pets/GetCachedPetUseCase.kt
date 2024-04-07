package com.mobdao.domain.usecases.pets

import com.mobdao.domain.api.repositories.PetsRepository
import com.mobdao.domain.models.Pet
import com.mobdao.domain.utils.mappers.PetMapper
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