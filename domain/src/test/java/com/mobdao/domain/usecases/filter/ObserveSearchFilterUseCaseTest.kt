package com.mobdao.domain.usecases.filter

import com.mobdao.common.testutils.mockfactories.domain.SearchFilterMockFactory
import com.mobdao.common.testutils.mockfactories.domain.entities.SearchFilterEntityMockFactory
import com.mobdao.domain.dataapi.repositories.SearchFilterRepository
import com.mobdao.domain.models.SearchFilter
import com.mobdao.domain.internal.SearchFilterEntity
import com.mobdao.domain.internal.mappers.SearchFilterMapper
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ObserveSearchFilterUseCaseTest {

    private val searchFilterEntity: SearchFilterEntity = SearchFilterEntityMockFactory.create()
    private val searchFilterUseCaseModel: SearchFilter = SearchFilterMockFactory.create()

    private val searchFilterRepository: SearchFilterRepository = mockk {
        every { observeSearchFilter() } returns flowOf(searchFilterEntity)
    }
    private val searchFilterMapper: SearchFilterMapper = mockk {
        every { mapFromEntity(searchFilterEntity) } returns searchFilterUseCaseModel
    }

    private val tested = ObserveSearchFilterUseCase(
        searchFilterRepository = searchFilterRepository,
        searchFilterMapper = searchFilterMapper,
    )

    @Test
    fun `given search filter is null when executed then null is returned`() = runTest {
        // given
        every { searchFilterRepository.observeSearchFilter() } returns flowOf(null)

        // when
        val result: SearchFilter? = tested.execute().first()

        // then
        assertNull(result)
    }

    @Test
    fun `given search filter is not null when executed then search filter is returned`() = runTest {
        // given / when
        val result: SearchFilter? = tested.execute().first()

        // then
        assertEquals(result, searchFilterUseCaseModel)
    }
}