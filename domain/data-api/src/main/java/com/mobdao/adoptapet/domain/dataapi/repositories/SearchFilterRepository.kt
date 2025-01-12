package com.mobdao.adoptapet.domain.dataapi.repositories

import com.mobdao.adoptapet.domain.entities.SearchFilterEntity
import kotlinx.coroutines.flow.Flow

interface SearchFilterRepository {
    fun saveSearchFilter(searchFilter: SearchFilterEntity?)

    fun getSearchFilter(): SearchFilterEntity?

    fun observeSearchFilter(): Flow<SearchFilterEntity?>
}
