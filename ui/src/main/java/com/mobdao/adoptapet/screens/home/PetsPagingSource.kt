package com.mobdao.adoptapet.screens.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobdao.adoptapet.screens.home.HomeViewModel.Pet
import com.mobdao.adoptapet.screens.home.HomeViewModel.Pet.Breeds
import com.mobdao.domain.GetPetsUseCase
import com.mobdao.domain.models.SearchFilter
import kotlinx.coroutines.flow.first
import timber.log.Timber
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
        return try {
            val pets: List<Pet> = getPetsUseCase
                .execute(pageNumber = pageNumber, searchFilter = searchFilter)
                .first()
                .map {
                    Pet(
                        id = it.id,
                        name = it.name,
                        breeds = Breeds(
                            primary = it.breeds.primary,
                            secondary = it.breeds.secondary,
                        ),
                        thumbnailUrl = it.photos.firstOrNull()?.smallUrl.orEmpty()
                    )
                }
            LoadResult.Page(
                data = pets,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (pets.isEmpty()) null else pageNumber + 1,
            )
        } catch (exception: Exception) {
            Timber.e(exception)
            LoadResult.Error(exception)
        }
    }

    class Factory @Inject constructor(private val getPetsUseCase: GetPetsUseCase) {

        fun create(
            isReadyToLoad: Boolean,
            searchFilter: SearchFilter?
        ): PetsPagingSource =
            PetsPagingSource(isReadyToLoad, getPetsUseCase, searchFilter)
    }
}
