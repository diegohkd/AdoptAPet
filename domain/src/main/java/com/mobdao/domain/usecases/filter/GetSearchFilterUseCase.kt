package com.mobdao.domain.usecases.filter

import com.mobdao.domain.dataapi.repositories.SearchFilterRepository
import com.mobdao.domain.models.SearchFilter
import com.mobdao.domain.internal.mappers.SearchFilterMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSearchFilterUseCase @Inject internal constructor(
    private val searchFilterRepository: SearchFilterRepository,
    private val searchFilterMapper: SearchFilterMapper,
) {

    fun execute(): Flow<SearchFilter?> = flow {
        emit(searchFilterRepository.getSearchFilter()?.let(searchFilterMapper::mapFromEntity))
    }
}
