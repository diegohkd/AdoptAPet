package com.mobdao.local

import com.mobdao.adoptapet.domain.entities.SearchFilterEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchFilterLocalDataSource
    @Inject
    internal constructor() {
        // In-memory caching
        private val searchFilter = MutableStateFlow<SearchFilterEntity?>(null)

        fun saveSearchFilter(searchFilter: SearchFilterEntity?) {
            this.searchFilter.value = searchFilter
        }

        fun getSearchFilter(): SearchFilterEntity? = searchFilter.value

        fun observeSearchFilter(): StateFlow<SearchFilterEntity?> = searchFilter
    }
