package com.mobdao.domain

import com.mobdao.domain.common_models.Pet
import com.mobdao.domain.common_models.SearchFilter
import com.mobdao.domain.utils.mappers.PetMapper
import com.mobdao.domain.utils.mappers.SearchFilterMapper
import com.mobdao.domain_api.repositories.PetsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPetsUseCase @Inject constructor(
    private val petsRepository: PetsRepository,
    private val petMapper: PetMapper,
    private val searchFilterMapper: SearchFilterMapper
) {

    fun execute(pageNumber: Int, searchFilter: SearchFilter?): Flow<List<Pet>> = flow {
        emit(
            petsRepository.getPets(
                pageNumber = pageNumber,
                searchFilter = searchFilter?.let(searchFilterMapper::mapToEntity)
            )
                .map(petMapper::map)
        )
    }
}