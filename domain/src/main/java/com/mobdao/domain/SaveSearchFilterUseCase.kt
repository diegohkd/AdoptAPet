package com.mobdao.domain

import com.mobdao.domain.api.repositories.SearchFilterRepository
import com.mobdao.domain.models.SearchFilter
import com.mobdao.domain.utils.mappers.SearchFilterMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveSearchFilterUseCase @Inject constructor(
    private val searchFilterRepository: SearchFilterRepository,
    private val searchFilterMapper: SearchFilterMapper,
) {

    fun execute(filter: SearchFilter): Flow<Unit> = flow {
        searchFilterRepository.saveSearchFilter(searchFilterMapper.mapToEntity(filter))
        emit(Unit)
    }
}
