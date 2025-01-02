package com.mobdao.adoptapet.domain.dataapi.repositories

import com.mobdao.adoptapet.domain.entities.SearchFilter
import kotlinx.coroutines.flow.Flow

interface SearchFilterRepository {
    fun saveSearchFilter(searchFilter: SearchFilter?)

    fun getSearchFilter(): SearchFilter?

    fun observeSearchFilter(): Flow<SearchFilter?>
}
