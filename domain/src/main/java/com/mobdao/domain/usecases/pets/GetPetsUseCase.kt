package com.mobdao.domain.usecases.pets

import com.mobdao.domain.api.repositories.PetsRepository
import com.mobdao.domain.models.Pet
import com.mobdao.domain.models.SearchFilter
import com.mobdao.domain.utils.mappers.PetMapper
import com.mobdao.domain.utils.mappers.SearchFilterMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPetsUseCase @Inject constructor(
    private val petsRepository: PetsRepository,
    private val petMapper: PetMapper,
    private val searchFilterMapper: SearchFilterMapper,
) {

    fun execute(pageNumber: Int, searchFilter: SearchFilter): Flow<List<Pet>> = flow {
        emit(
            petsRepository.getPets(
                pageNumber = pageNumber,
                searchFilter = searchFilterMapper.mapToEntity(searchFilter)
            )
                .map(petMapper::map)
        )
    }
}
