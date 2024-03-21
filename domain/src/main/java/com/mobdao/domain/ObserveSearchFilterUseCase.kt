package com.mobdao.domain

import com.mobdao.domain.api.repositories.SearchFilterRepository
import com.mobdao.domain.models.SearchFilter
import com.mobdao.domain.utils.mappers.SearchFilterMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveSearchFilterUseCase @Inject constructor(
    private val searchFilterRepository: SearchFilterRepository,
    private val searchFilterMapper: SearchFilterMapper,
) {

    fun execute(): Flow<SearchFilter?> =
        searchFilterRepository.observeSearchFilter()
            .map { it?.let(searchFilterMapper::mapFromEntity) }
}
