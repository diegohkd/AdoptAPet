package com.mobdao.domain.usecases.filter

import com.mobdao.common.testutils.mockfactories.domain.AddressMockFactory
import com.mobdao.common.testutils.mockfactories.domain.SearchFilterMockFactory
import com.mobdao.common.testutils.mockfactories.domain.entities.AddressEntityMockFactory
import com.mobdao.domain.dataapi.repositories.GeoLocationRepository
import com.mobdao.domain.dataapi.repositories.SearchFilterRepository
import com.mobdao.domain.internal.AddressEntity
import com.mobdao.domain.internal.SearchFilterEntity
import com.mobdao.domain.internal.mappers.AddressMapper
import com.mobdao.domain.internal.mappers.SearchFilterMapper
import com.mobdao.domain.models.Address
import com.mobdao.domain.models.SearchFilter
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SaveSearchFilterUseCaseTest {
    private val address: Address = AddressMockFactory.create()
    private val searchFilter: SearchFilter =
        SearchFilterMockFactory.create(
            address = address,
        )
    private val addressEntity: AddressEntity = AddressEntityMockFactory.create()
    private val searchFilterEntity: SearchFilterEntity = mockk()

    private val searchFilterRepository: SearchFilterRepository =
        mockk {
            justRun { saveSearchFilter(searchFilterEntity) }
        }
    private val geoLocationRepository: GeoLocationRepository =
        mockk {
            coJustRun { cacheCurrentLocationAddress(addressEntity) }
        }
    private val addressMapper: AddressMapper =
        mockk {
            every { map(address) } returns addressEntity
        }
    private val searchFilterMapper: SearchFilterMapper =
        mockk {
            every { mapToEntity(searchFilter) } returns searchFilterEntity
        }

    private val tested =
        SaveSearchFilterUseCase(
            searchFilterRepository = searchFilterRepository,
            geoLocationRepository = geoLocationRepository,
            addressMapper = addressMapper,
            searchFilterMapper = searchFilterMapper,
        )

    @Test
    fun `when executed then filter address is cached`() =
        runTest {
            // when
            tested.execute(searchFilter).first()

            // then
            coVerify { geoLocationRepository.cacheCurrentLocationAddress(addressEntity) }
        }

    @Test
    fun `when executed then filter is cached`() =
        runTest {
            // when
            tested.execute(searchFilter).first()

            // then
            coVerify { searchFilterRepository.saveSearchFilter(searchFilterEntity) }
        }
}
