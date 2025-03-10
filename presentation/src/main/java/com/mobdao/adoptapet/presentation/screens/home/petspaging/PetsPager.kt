package com.mobdao.adoptapet.presentation.screens.home.petspaging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mobdao.adoptapet.domain.models.SearchFilter
import com.mobdao.adoptapet.presentation.common.utils.factories.PagerFactory
import com.mobdao.adoptapet.presentation.screens.home.HomeUiState.PetState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

private const val ITEMS_PER_PAGE = 20

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
class PetsPager
    @Inject
    constructor(
        private val pagerFactory: PagerFactory,
        private val petsPagingSourceFactory: PetsPagingSource.Factory,
    ) {
        private val searchFilter = MutableStateFlow<SearchFilter?>(null)

        val items: Flow<PagingData<PetState>> =
            searchFilter
                .filterNotNull()
                .flatMapLatest { searchFilter ->
                    pagerFactory
                        .create(
                            config =
                                PagingConfig(
                                    pageSize = ITEMS_PER_PAGE,
                                    enablePlaceholders = false,
                                ),
                            pagingSourceFactory = {
                                petsPagingSourceFactory.create(searchFilter = searchFilter)
                            },
                        ).flow
                }

        fun setFilterAndRefresh(searchFilter: SearchFilter) {
            this.searchFilter.value = searchFilter
        }
    }
