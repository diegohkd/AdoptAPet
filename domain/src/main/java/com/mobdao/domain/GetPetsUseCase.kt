package com.mobdao.domain

import com.mobdao.domain.common_models.Pet
import com.mobdao.domain.utils.mappers.PetMapper
import com.mobdao.domain_api.PetsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPetsUseCase @Inject constructor(
    private val petsRepository: PetsRepository,
    private val petMapper: PetMapper,
) {

    // TODO should return Pet entity instead of a new model?
    fun execute(): Flow<List<Pet>> = flow {
        emit(petsRepository.getPets().map(petMapper::map))
    }
}