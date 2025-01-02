package com.mobdao.adoptapet.domain.usecases.pets

import com.mobdao.adoptapet.domain.dataapi.repositories.PetsRepository
import com.mobdao.adoptapet.domain.internal.mappers.PetMapper
import com.mobdao.adoptapet.domain.internal.mappers.SearchFilterMapper
import com.mobdao.adoptapet.domain.models.Pet
import com.mobdao.adoptapet.domain.models.SearchFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPetsUseCase
    @Inject
    internal constructor(
        private val petsRepository: PetsRepository,
        private val petMapper: PetMapper,
        private val searchFilterMapper: SearchFilterMapper,
    ) {
        fun execute(
            pageNumber: Int,
            searchFilter: SearchFilter,
        ): Flow<List<Pet>> =
            flow {
                emit(
                    petsRepository
                        .getPets(
                            pageNumber = pageNumber,
                            searchFilter = searchFilterMapper.mapToEntity(searchFilter),
                        ).map(petMapper::map),
                )
            }
    }
