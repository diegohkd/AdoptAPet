package com.mobdao.domain.usecases.filter

import com.mobdao.domain.dataapi.repositories.SearchFilterRepository
import com.mobdao.domain.internal.mappers.SearchFilterMapper
import com.mobdao.domain.models.SearchFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveSearchFilterUseCase
    @Inject
    internal constructor(
        private val searchFilterRepository: SearchFilterRepository,
        private val searchFilterMapper: SearchFilterMapper,
    ) {
        fun execute(): Flow<SearchFilter?> =
            searchFilterRepository
                .observeSearchFilter()
                .map { it?.let(searchFilterMapper::mapFromEntity) }
    }
