package com.mobdao.local

import com.mobdao.adoptapet.domain.entities.SearchFilterEntity
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchFilterEntityLocalDataSourceTest {
    private val searchFilter: SearchFilterEntity = mockk()

    private val tested = SearchFilterLocalDataSource()

    @Test
    fun `given search filter is saved when get search filter then search filter is returned`() {
        // given
        tested.saveSearchFilter(searchFilter)

        // when
        val result: SearchFilterEntity? = tested.getSearchFilter()

        // then
        assertEquals(result, searchFilter)
    }

    @Test
    fun `given it is observing search filter and saved filter is saved when saving another search filter then it is returned`() =
        runTest {
            // given
            val observingSearchFilter = tested.observeSearchFilter()
            tested.saveSearchFilter(searchFilter)
            observingSearchFilter.first()
            val searchFilter2: SearchFilterEntity = mockk()

            // when
            tested.saveSearchFilter(searchFilter2)

            // then
            assertEquals(observingSearchFilter.first(), searchFilter2)
        }
}
