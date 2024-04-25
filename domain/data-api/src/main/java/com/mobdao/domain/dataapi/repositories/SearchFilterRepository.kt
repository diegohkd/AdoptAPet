package com.mobdao.domain.dataapi.repositories

import com.mobdao.domain.entities.SearchFilter
import kotlinx.coroutines.flow.Flow

interface SearchFilterRepository {

    fun saveSearchFilter(searchFilter: SearchFilter?)
    fun getSearchFilter(): SearchFilter?
    fun observeSearchFilter(): Flow<SearchFilter?>
}
