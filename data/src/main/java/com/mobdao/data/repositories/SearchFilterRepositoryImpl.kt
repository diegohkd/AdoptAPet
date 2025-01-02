package com.mobdao.data.repositories

import com.mobdao.adoptapet.domain.dataapi.repositories.SearchFilterRepository
import com.mobdao.adoptapet.domain.entities.SearchFilterEntity
import com.mobdao.local.SearchFilterLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchFilterRepositoryImpl
    @Inject
    constructor(
        private val searchFilterLocalDataSource: SearchFilterLocalDataSource,
    ) : SearchFilterRepository {
        override fun saveSearchFilter(searchFilter: SearchFilterEntity?) {
            searchFilterLocalDataSource.saveSearchFilter(searchFilter)
        }

        override fun getSearchFilter(): SearchFilterEntity? = searchFilterLocalDataSource.getSearchFilter()

        override fun observeSearchFilter(): Flow<SearchFilterEntity?> = searchFilterLocalDataSource.observeSearchFilter()
    }
