package com.mobdao.data

import com.mobdao.cache.SearchFilterHolder
import com.mobdao.data.utils.mappers.SearchFilterMapper
import com.mobdao.domain_api.SearchFilterRepository
import com.mobdao.domain_api.entitites.SearchFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchFilterRepositoryImpl @Inject constructor(
    private val searchFilterHolder: SearchFilterHolder,
    private val searchFilterMapper: SearchFilterMapper,
) : SearchFilterRepository {

    override fun saveSearchFilter(searchFilter: SearchFilter?) {
        searchFilterHolder.set(searchFilter?.let(searchFilterMapper::mapToCacheModel))
    }

    override fun observeSearchFilter(): Flow<SearchFilter?> =
        searchFilterHolder
            .observe()
            .map { it?.let(searchFilterMapper::mapFromCacheModel) }
            .distinctUntilChanged()
}