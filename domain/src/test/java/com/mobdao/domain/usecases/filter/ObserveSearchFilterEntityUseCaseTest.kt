package com.mobdao.domain.usecases.filter

import com.mobdao.adoptapet.common.testutils.domain.SearchFilterMockFactory
import com.mobdao.adoptapet.common.testutils.domain.entities.SearchFilterEntityMockFactory
import com.mobdao.adoptapet.domain.dataapi.repositories.SearchFilterRepository
import com.mobdao.adoptapet.domain.entities.SearchFilterEntity
import com.mobdao.adoptapet.domain.internal.mappers.SearchFilterMapper
import com.mobdao.adoptapet.domain.models.SearchFilter
import com.mobdao.adoptapet.domain.usecases.filter.ObserveSearchFilterUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ObserveSearchFilterEntityUseCaseTest {
    private val searchFilterEntity: SearchFilterEntity = SearchFilterEntityMockFactory.create()
    private val searchFilterUseCaseModel: SearchFilter = SearchFilterMockFactory.create()

    private val searchFilterRepository: SearchFilterRepository =
        mockk {
            every { observeSearchFilter() } returns flowOf(searchFilterEntity)
        }
    private val searchFilterMapper: SearchFilterMapper =
        mockk {
            every { mapFromEntity(searchFilterEntity) } returns searchFilterUseCaseModel
        }

    private val tested =
        ObserveSearchFilterUseCase(
            searchFilterRepository = searchFilterRepository,
            searchFilterMapper = searchFilterMapper,
        )

    @Test
    fun `given search filter is null when executed then null is returned`() =
        runTest {
            // given
            every { searchFilterRepository.observeSearchFilter() } returns flowOf(null)

            // when
            val result: SearchFilter? = tested.execute().first()

            // then
            assertNull(result)
        }

    @Test
    fun `given search filter is not null when executed then search filter is returned`() =
        runTest {
            // given / when
            val result: SearchFilter? = tested.execute().first()

            // then
            assertEquals(result, searchFilterUseCaseModel)
        }
}
