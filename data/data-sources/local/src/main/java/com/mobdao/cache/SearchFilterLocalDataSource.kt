package com.mobdao.cache

import com.mobdao.cache.models.SearchFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchFilterLocalDataSource @Inject internal constructor() {

    // In-memory caching
    private val searchFilter = MutableStateFlow<SearchFilter?>(null)

    fun saveSearchFilter(searchFilter: SearchFilter?) {
        this.searchFilter.value = searchFilter
    }

    fun observeSearchFilter(): StateFlow<SearchFilter?> = searchFilter
}