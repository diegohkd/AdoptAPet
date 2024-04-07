package com.mobdao.data.repositories

import com.mobdao.cache.SearchFilterLocalDataSource
import com.mobdao.common.testutils.mockfactories.data.local.SearchFilterCacheMockFactory
import com.mobdao.common.testutils.mockfactories.domain.api.entities.SearchFilterEntityMockFactory
import com.mobdao.data.common.SearchFilterCache
import com.mobdao.data.utils.mappers.SearchFilterMapper
import com.mobdao.domain.api.entitites.SearchFilter
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

    private val searchFilter: SearchFilter = SearchFilterEntityMockFactory.create()
    private val searchFilterCache: SearchFilterCache = SearchFilterCacheMockFactory.create()

    private val searchFilterLocalDataSource: SearchFilterLocalDataSource = mockk {
        justRun { saveSearchFilter(searchFilterCache) }
        every { getSearchFilter() } returns searchFilterCache
        every { observeSearchFilter() } returns MutableStateFlow(searchFilterCache)
    }
    private val searchFilterMapper: SearchFilterMapper = mockk {
        every { mapToCacheModel(searchFilter) } returns searchFilterCache
        every { mapFromCacheModel(searchFilterCache) } returns searchFilter
    }

    private val tested = SearchFilterRepositoryImpl(
        searchFilterLocalDataSource = searchFilterLocalDataSource,
        searchFilterMapper = searchFilterMapper,
    )

    @Test
    fun `given search filter when save search filter then filter is saved`() {
        // given / when
        tested.saveSearchFilter(searchFilter)

        // then
        verify { searchFilterLocalDataSource.saveSearchFilter(searchFilterCache) }
    }

    @Test
    fun `when get search filter then search filter is returned`() {
        // when
        val result: SearchFilter? = tested.getSearchFilter()

        // then
        assertEquals(result, searchFilter)
    }

    @Test
    fun `when observe search filter then search filter is returned`() = runTest {
        // when
        val result: SearchFilter? = tested.observeSearchFilter().first()

        // then
        assertEquals(result, searchFilter)
    }
}