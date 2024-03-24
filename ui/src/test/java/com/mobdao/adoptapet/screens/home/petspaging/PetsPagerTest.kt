package com.mobdao.adoptapet.screens.home.petspaging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingSource
import com.mobdao.adoptapet.screens.home.HomeViewModel.Pet
import com.mobdao.adoptapet.utils.PagerFactory
import com.mobdao.domain.models.SearchFilter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class PetsPagerTest {

    private val petsPagingSource: PetsPagingSource = mockk()
    private val pager: Pager<Int, Pet> = mockk {
        every { flow } returns flowOf(mockk())
    }

    private val pagerFactory: PagerFactory = mockk {
        every { create<Int, Pet>(config = any(), pagingSourceFactory = any()) } returns pager
    }
    private val petsPagingSourceFactory: PetsPagingSource.Factory = mockk {
        every { create(any()) } returns petsPagingSource
    }

    private val tested by lazy {
        PetsPager(
            pagerFactory = pagerFactory,
            petsPagingSourceFactory = petsPagingSourceFactory,
        )
    }

    @Test
    fun `given no search filter set when observing items then paging source is created with null filter`() =
        runTest {
            // given
            every {
                pagerFactory.create<Int, Pet>(
                    config = any(),
                    pagingSourceFactory = any()
                )
            } answers {
                lastArg<() -> PagingSource<Int, Pet>>()()
                pager
            }

            // when
            tested.items.firstOrNull()

            // then
            verify { petsPagingSourceFactory.create(searchFilter = null) }
        }

    @Test
    fun `given search filter set when observing items then paging source is re-created with search filter passed`() =
        runTest {
            // given
            every {
                pagerFactory.create<Int, Pet>(
                    config = any(),
                    pagingSourceFactory = any()
                )
            } answers {
                lastArg<() -> PagingSource<Int, Pet>>()()
                pager
            }
            tested.items.firstOrNull()
            val searchFilter: SearchFilter = mockk()
            tested.setFilterAndRefresh(searchFilter)

            // then
            tested.items.firstOrNull()

            // then
            verifyOrder {
                petsPagingSourceFactory.create(searchFilter = null)
                petsPagingSourceFactory.create(searchFilter = searchFilter)
            }
        }

    @Test
    fun `given filter not set when checking if pager is ready then false is returned`() {
        // given / when
        val result = tested.isReady()

        // then
        assertFalse(result)
    }

    @Test
    fun `given filter set and list refreshed when checking if pager is ready then true is returned`() {
        // given
        tested.setFilterAndRefresh(mockk())

        // when
        val result = tested.isReady()

        // then
        assertTrue(result)
    }
}