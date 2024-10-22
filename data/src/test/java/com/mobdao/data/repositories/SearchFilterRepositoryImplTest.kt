package com.mobdao.data.repositories

import com.mobdao.domain.entities.SearchFilter
import com.mobdao.local.SearchFilterLocalDataSource
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchFilterRepositoryImplTest {
    private val searchFilter: SearchFilter = mockk()

    private val searchFilterLocalDataSource: SearchFilterLocalDataSource =
        mockk {
            justRun { saveSearchFilter(searchFilter) }
            every { getSearchFilter() } returns searchFilter
            every { observeSearchFilter() } returns MutableStateFlow(searchFilter)
        }

    private val tested =
        SearchFilterRepositoryImpl(
            searchFilterLocalDataSource = searchFilterLocalDataSource,
        )

    @Test
    fun `given search filter when save search filter then filter is saved`() {
        // given / when
        tested.saveSearchFilter(searchFilter)

        // then
        verify { searchFilterLocalDataSource.saveSearchFilter(searchFilter) }
    }

    @Test
    fun `when get search filter then search filter is returned`() {
        // when
        val result: SearchFilter? = tested.getSearchFilter()

        // then
        assertEquals(result, searchFilter)
    }

    @Test
    fun `when observe search filter then search filter is returned`() =
        runTest {
            // when
            val result: SearchFilter? = tested.observeSearchFilter().first()

            // then
            assertEquals(result, searchFilter)
        }
}
