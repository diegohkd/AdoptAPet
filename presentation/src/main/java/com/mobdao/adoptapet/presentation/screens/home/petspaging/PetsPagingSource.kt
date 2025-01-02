package com.mobdao.adoptapet.presentation.screens.home.petspaging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobdao.adoptapet.presentation.screens.home.HomeUiState.BreedsState
import com.mobdao.adoptapet.presentation.screens.home.HomeUiState.PetState
import com.mobdao.domain.models.Pet
import com.mobdao.domain.models.SearchFilter
import com.mobdao.domain.usecases.pets.GetPetsUseCase
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val DEFAULT_INITIAL_PAGE_NUMBER = 1

class PetsPagingSource private constructor(
    private val getPetsUseCase: GetPetsUseCase,
    private val searchFilter: SearchFilter,
) : PagingSource<Int, PetState>() {
    override fun getRefreshKey(state: PagingState<Int, PetState>): Int? {
        // Force to always start from first page on refresh
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PetState> {
        val pageNumber: Int = params.key ?: DEFAULT_INITIAL_PAGE_NUMBER
        // TODO handle page size
        return try {
            val pets: List<PetState> =
                getPetsUseCase
                    .execute(pageNumber = pageNumber, searchFilter = searchFilter)
                    .first()
                    .map { it.toViewModelModel() }
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

    private fun Pet.toViewModelModel(): PetState =
        PetState(
            id = id,
            type = type,
            name = name,
            breeds =
                BreedsState(
                    primary = breeds.primary,
                    secondary = breeds.secondary,
                ),
            thumbnailUrl = photos.firstOrNull()?.smallUrl.orEmpty(),
        )

    @Singleton
    class Factory
        @Inject
        constructor(
            private val getPetsUseCase: GetPetsUseCase,
        ) {
            fun create(searchFilter: SearchFilter): PetsPagingSource = PetsPagingSource(getPetsUseCase, searchFilter)
        }
}
