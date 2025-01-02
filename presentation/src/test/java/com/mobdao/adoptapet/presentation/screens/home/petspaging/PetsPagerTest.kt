package com.mobdao.adoptapet.presentation.screens.home.petspaging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingSource
import com.mobdao.adoptapet.presentation.screens.home.HomeUiState.PetState
import com.mobdao.adoptapet.presentation.utils.PagerFactory
import com.mobdao.common.testutils.MainDispatcherRule
import com.mobdao.domain.models.SearchFilter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalPagingApi::class, FlowPreview::class)
class PetsPagerTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private val petsPagingSource: PetsPagingSource = mockk()
    private val pager: Pager<Int, PetState> =
        mockk {
            every { flow } returns flowOf(mockk())
        }

    private val pagerFactory: PagerFactory =
        mockk {
            every {
                create<Int, PetState>(
                    config = any(),
                    pagingSourceFactory = any(),
                )
            } returns pager
        }
    private val petsPagingSourceFactory: PetsPagingSource.Factory =
        mockk {
            every { create(any()) } returns petsPagingSource
        }

    private val tested by lazy {
        PetsPager(
            pagerFactory = pagerFactory,
            petsPagingSourceFactory = petsPagingSourceFactory,
        )
    }

    @Test
    fun `given no search filter set when observing items then paging source is not created and no paging data is returned`() =
        runTest {
            // given / when
            val pagingData =
                try {
                    tested.items.timeout(0.milliseconds).first()
                } catch (e: Exception) {
                    null
                }

            // then
            assertNull(pagingData)
            verify(exactly = 0) { petsPagingSourceFactory.create(any()) }
        }

    @Test
    fun `given search filter set when observing items then paging source is created with search filter passed`() =
        runTest {
            // given
            every {
                pagerFactory.create<Int, PetState>(
                    config = any(),
                    pagingSourceFactory = any(),
                )
            } answers {
                lastArg<() -> PagingSource<Int, PetState>>()()
                pager
            }
            val searchFilter: SearchFilter = mockk()
            tested.setFilterAndRefresh(searchFilter)

            // then
            tested.items.firstOrNull()

            // then
            verifyOrder {
                petsPagingSourceFactory.create(searchFilter = searchFilter)
            }
        }
}
