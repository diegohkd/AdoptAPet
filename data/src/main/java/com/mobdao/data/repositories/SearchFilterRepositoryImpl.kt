package com.mobdao.data.repositories

import com.mobdao.cache.SearchFilterLocalDataSource
import com.mobdao.data.utils.mappers.SearchFilterMapper
import com.mobdao.domain_api.repositories.SearchFilterRepository
import com.mobdao.domain_api.entitites.SearchFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchFilterRepositoryImpl @Inject constructor(
    private val searchFilterLocalDataSource: SearchFilterLocalDataSource,
    private val searchFilterMapper: SearchFilterMapper,
) : SearchFilterRepository {

    override fun saveSearchFilter(searchFilter: SearchFilter?) {
        searchFilterLocalDataSource.saveSearchFilter(searchFilter?.let(searchFilterMapper::mapToCacheModel))
    }

    override fun getSearchFilter(): SearchFilter? =
        searchFilterLocalDataSource.getSearchFilter()
            ?.let(searchFilterMapper::mapFromCacheModel)

    override fun observeSearchFilter(): Flow<SearchFilter?> =
        searchFilterLocalDataSource
            .observeSearchFilter()
            .map { it?.let(searchFilterMapper::mapFromCacheModel) }
            .distinctUntilChanged()
}