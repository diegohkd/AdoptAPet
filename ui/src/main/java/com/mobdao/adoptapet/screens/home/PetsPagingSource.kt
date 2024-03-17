package com.mobdao.adoptapet.screens.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobdao.adoptapet.screens.home.HomeViewModel.Pet
import com.mobdao.domain.GetPetsUseCase
import com.mobdao.domain.common_models.SearchFilter
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PetsPagingSource private constructor(
    private val isReadyToLoad: Boolean,
    private val getPetsUseCase: GetPetsUseCase,
    private val searchFilter: SearchFilter?,
) : PagingSource<Int, Pet>() {

    override fun getRefreshKey(state: PagingState<Int, Pet>): Int? {
        // Force to always start from first page on refresh
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pet> {
        if (!isReadyToLoad) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null,
            )
        }
        val pageNumber: Int = params.key ?: 1
        // TODO handle page size
        // TODO handle errors
        val pets: List<Pet> = getPetsUseCase
            .execute(pageNumber = pageNumber, searchFilter = searchFilter)
            .first()
            .map {
                Pet(
                    id = it.id,
                    name = it.name,
                    thumbnailUrl = it.photos.firstOrNull()?.smallUrl.orEmpty()
                )
            }
        return LoadResult.Page(
            data = pets,
            prevKey = if (pageNumber == 1) null else pageNumber - 1,
            nextKey = if (pets.isEmpty()) null else pageNumber + 1,
        )
    }

    class Factory @Inject constructor(private val getPetsUseCase: GetPetsUseCase) {

        fun create(
            isReadyToLoad: Boolean,
            searchFilter: SearchFilter?
        ): PetsPagingSource =
            PetsPagingSource(isReadyToLoad, getPetsUseCase, searchFilter)
    }
}