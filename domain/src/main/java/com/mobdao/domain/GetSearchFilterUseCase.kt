package com.mobdao.domain

import com.mobdao.domain.common_models.SearchFilter
import com.mobdao.domain.utils.mappers.SearchFilterMapper
import com.mobdao.domain_api.SearchFilterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSearchFilterUseCase @Inject constructor(
    private val searchFilterRepository: SearchFilterRepository,
    private val searchFilterMapper: SearchFilterMapper,
) {

    fun execute(): Flow<SearchFilter?> = flow {
        emit(searchFilterRepository.getSearchFilter()?.let(searchFilterMapper::mapFromEntity))
    }
}