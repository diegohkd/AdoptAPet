package com.mobdao.adoptapet.domain.usecases.filter

import com.mobdao.adoptapet.domain.dataapi.repositories.SearchFilterRepository
import com.mobdao.adoptapet.domain.internal.mappers.SearchFilterMapper
import com.mobdao.adoptapet.domain.models.SearchFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSearchFilterUseCase
    @Inject
    internal constructor(
        private val searchFilterRepository: SearchFilterRepository,
        private val searchFilterMapper: SearchFilterMapper,
    ) {
        fun execute(): Flow<SearchFilter?> =
            flow {
                emit(searchFilterRepository.getSearchFilter()?.let(searchFilterMapper::mapFromEntity))
            }
    }
