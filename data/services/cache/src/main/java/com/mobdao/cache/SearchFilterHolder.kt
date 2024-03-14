package com.mobdao.cache

import com.mobdao.cache.models.SearchFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchFilterHolder @Inject constructor() {

    // Just caching in the memory
    private val searchFilter = MutableStateFlow<SearchFilter?>(null)

    fun set(searchFilter: SearchFilter?) {
        this.searchFilter.value = searchFilter
    }

    fun observe(): StateFlow<SearchFilter?> = searchFilter
}