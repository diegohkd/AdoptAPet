package com.mobdao.adoptapet.screens.home.petspaging

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingSource.LoadResult.Error
import androidx.paging.PagingSource.LoadResult.Page
import com.mobdao.adoptapet.screens.home.HomeViewModel.Pet
import com.mobdao.adoptapet.utils.PetDomain
import com.mobdao.common.testutils.mockfactories.domain.BreedsMockFactory
import com.mobdao.common.testutils.mockfactories.domain.PetMockFactory
import com.mobdao.common.testutils.mockfactories.domain.PhotoMockFactory
import com.mobdao.domain.models.AnimalType.DOG
import com.mobdao.domain.models.SearchFilter
import com.mobdao.domain.usecases.pets.GetPetsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PetsPagingSourceTest {

    private val params: LoadParams<Int> = mockk {
        every { key } returns null
    }
    private val pet: PetDomain = PetMockFactory.create(
        id = "id",
        type = DOG,
        name = "name",
        breeds = BreedsMockFactory.create(
            primary = "primary",
            secondary = "secondary",
        ),
        photos = listOf(
            PhotoMockFactory.create(smallUrl = "smallUrl")
        ),
    )
    private val searchFilter: SearchFilter = mockk()

    private val getPetsUseCase: GetPetsUseCase = mockk {
        every { execute(pageNumber = 1, searchFilter = searchFilter) } returns flowOf(listOf(pet))
    }

    private val tested by lazy { createTested(searchFilter = searchFilter) }

    @Test
    fun `given any page state when get refresh key then null is returned`() {
        // given / when
        val result: Int? = tested.getRefreshKey(state = mockk())

        // then
        assertNull(result)
    }

    @Test
    fun `given getting pets throws an exception when load then error result is returned`() =
        runTest {
            // given
            val exception = RuntimeException()
            every {
                getPetsUseCase.execute(
                    pageNumber = any(),
                    searchFilter = any()
                )
            } returns flow { throw exception }

            // when
            val result: LoadResult<Int, Pet> = tested.load(params)

            // then
            assertEquals(
                result,
                Error<Int, Pet>(exception)
            )
        }

    @Test
    fun `given load params has null key when load then first page is fetched`() =
        runTest {
            // given / when
            tested.load(params)

            // then
            verify { getPetsUseCase.execute(pageNumber = 1, searchFilter = searchFilter) }
        }

    @Test
    fun `given loading first page when load then previous key is null`() = runTest {
        // given / when
        val result = tested.load(params)

        // then
        assertNull((result as Page).prevKey)
    }

    @Test
    fun `given loading second page when load then previous key is 1`() = runTest {
        // given
        val petsPage2 = listOf(
            PetMockFactory.create(
                id = "id-2",
                name = "name-2",
                breeds = BreedsMockFactory.create(
                    primary = "primary",
                    secondary = "secondary",
                ),
                photos = listOf(
                    PhotoMockFactory.create(smallUrl = "smallUrl")
                ),
            )
        )
        every {
            getPetsUseCase.execute(
                pageNumber = 2,
                searchFilter = searchFilter
            )
        } returns flowOf(petsPage2)
        every { params.key } returns 2

        // when
        val result = tested.load(params)

        // then
        assertEquals(
            (result as Page).prevKey,
            1
        )
    }

    @Test
    fun `given getting pets returns empty list when load then page result is returned with null next key`() =
        runTest {
            // given
            every {
                getPetsUseCase.execute(
                    pageNumber = any(),
                    searchFilter = any()
                )
            } returns flowOf(emptyList())

            // when
            val result: LoadResult<Int, Pet> = tested.load(params)

            // then
            assertNull((result as Page).nextKey)
        }

    @Test
    fun `given getting pets returns not empty list when load then page result is returned with null next key`() =
        runTest {
            // given / when
            val result: LoadResult<Int, Pet> = tested.load(params)

            // then
            assertEquals(
                (result as Page).nextKey,
                2
            )
        }

    @Test
    fun `given getting pets returns not empty list when load then page result with pets is returned`() =
        runTest {
            // given / when
            val result: LoadResult<Int, Pet> = tested.load(params)

            // then
            assertEquals(
                (result as Page).data,
                listOf(
                    Pet(
                        id = "id",
                        type = DOG,
                        name = "name",
                        breeds = Pet.Breeds(
                            primary = "primary",
                            secondary = "secondary",
                        ),
                        thumbnailUrl = "smallUrl"
                    )
                )
            )
        }

    private fun createTested(searchFilter: SearchFilter) =
        PetsPagingSource.Factory(getPetsUseCase = getPetsUseCase)
            .create(searchFilter = searchFilter)
}