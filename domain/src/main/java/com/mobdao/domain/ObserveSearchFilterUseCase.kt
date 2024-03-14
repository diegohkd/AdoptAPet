package com.mobdao.domain

import com.mobdao.domain.common_models.SearchFilter
import com.mobdao.domain.utils.mappers.SearchFilterMapper
import com.mobdao.domain_api.SearchFilterRepository
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