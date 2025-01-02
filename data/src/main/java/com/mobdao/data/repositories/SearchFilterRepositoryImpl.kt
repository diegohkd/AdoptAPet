package com.mobdao.data.repositories

import com.mobdao.adoptapet.domain.dataapi.repositories.SearchFilterRepository
import com.mobdao.adoptapet.domain.entities.SearchFilter
import com.mobdao.local.SearchFilterLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchFilterRepositoryImpl
    @Inject
    constructor(
        private val searchFilterLocalDataSource: SearchFilterLocalDataSource,
    ) : SearchFilterRepository {
        override fun saveSearchFilter(searchFilter: SearchFilter?) {
            searchFilterLocalDataSource.saveSearchFilter(searchFilter)
        }

        override fun getSearchFilter(): SearchFilter? = searchFilterLocalDataSource.getSearchFilter()

        override fun observeSearchFilter(): Flow<SearchFilter?> = searchFilterLocalDataSource.observeSearchFilter()
    }
