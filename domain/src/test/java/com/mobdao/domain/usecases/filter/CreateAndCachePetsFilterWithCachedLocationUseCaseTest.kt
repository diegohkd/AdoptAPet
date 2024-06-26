package com.mobdao.domain.usecases.filter

import com.mobdao.common.exceptions.LocationNotFoundException
import com.mobdao.common.testutils.mockfactories.domain.entities.AddressEntityMockFactory
import com.mobdao.domain.dataapi.repositories.GeoLocationRepository
import com.mobdao.domain.dataapi.repositories.SearchFilterRepository
import com.mobdao.domain.entities.Address
import com.mobdao.domain.internal.SearchFilterEntity
import io.mockk.coEvery
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CreateAndCachePetsFilterWithCachedLocationUseCaseTest {

    private val address: Address = AddressEntityMockFactory.create()

    private val geoLocationRepository: GeoLocationRepository = mockk {
        coEvery { getCachedCurrentLocationAddress() } returns address
    }
    private val searchFilterRepository: SearchFilterRepository = mockk {
        justRun { saveSearchFilter(any()) }
    }

    private val tested = CreateAndCachePetsFilterWithCachedLocationUseCase(
        geoLocationRepository = geoLocationRepository,
        searchFilterRepository = searchFilterRepository,
    )

    @Test(expected = LocationNotFoundException::class)
    fun `given cached current location is null when executed then LocationNotFoundException is thrown`() =
        runTest {
            // given
            coEvery { geoLocationRepository.getCachedCurrentLocationAddress() } returns null

            // when / then
            tested.execute().first()
        }

    @Test
    fun `given cached current location is not null when executed then search filter with current location is saved`() =
        runTest {
            // given / when
            tested.execute().first()

            // then
            verify {
                searchFilterRepository.saveSearchFilter(SearchFilterEntity(address = address))
            }
        }
}
