package com.mobdao.adoptapet.domain.usecases.pets

import com.mobdao.adoptapet.domain.dataapi.repositories.PetsRepository
import com.mobdao.adoptapet.domain.internal.mappers.PetMapper
import com.mobdao.adoptapet.domain.models.Pet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCachedPetUseCase
    @Inject
    internal constructor(
        private val petsRepository: PetsRepository,
        private val petMapper: PetMapper,
    ) {
        fun execute(petId: String): Flow<Pet?> =
            flow {
                emit(petsRepository.getCachedPetById(petId)?.let(petMapper::map))
            }
    }
