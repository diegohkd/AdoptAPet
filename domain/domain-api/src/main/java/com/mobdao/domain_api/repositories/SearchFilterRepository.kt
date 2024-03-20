package com.mobdao.domain_api.repositories

import com.mobdao.domain_api.entitites.SearchFilter
import kotlinx.coroutines.flow.Flow

interface SearchFilterRepository {

    fun saveSearchFilter(searchFilter: SearchFilter?)
    fun getSearchFilter(): SearchFilter?
    fun observeSearchFilter(): Flow<SearchFilter?>
}