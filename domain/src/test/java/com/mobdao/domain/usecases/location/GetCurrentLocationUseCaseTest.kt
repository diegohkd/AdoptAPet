package com.mobdao.domain.usecases.location

import com.mobdao.common.exceptions.CurrentLocationNotFoundException
import com.mobdao.common.testutils.mockfactories.domain.AddressMockFactory
import com.mobdao.common.testutils.mockfactories.domain.api.entities.AddressEntityMockFactory
import com.mobdao.domain.api.repositories.GeoLocationRepository
import com.mobdao.domain.models.Address
import com.mobdao.domain.utils.AddressEntity
import com.mobdao.domain.utils.mappers.AddressMapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCurrentLocationUseCaseTest {

    private val addressEntity: AddressEntity = AddressEntityMockFactory.create()
    private val address: Address = AddressMockFactory.create()

    private val geoLocationRepository: GeoLocationRepository = mockk {
        coEvery { getCurrentLocationAddress() } returns addressEntity
    }
    private val addressMapper: AddressMapper = mockk {
        every { map(addressEntity) } returns address
    }

    private val tested = GetCurrentLocationUseCase(
        geoLocationRepository = geoLocationRepository,
        addressMapper = addressMapper,
    )

    @Test(expected = CurrentLocationNotFoundException::class)
    fun `given null location is returned when executed then CurrentLocationNotFoundException is thrown`() =
        runTest {
            // given
            coEvery { geoLocationRepository.getCurrentLocationAddress() } returns null

            // when / then
            tested.execute().first()
        }

    @Test
    fun `given address is returned when executed then address is returned`() =
        runTest {
            // given / when
            val result: Address = tested.execute().first()

            // then
            assertEquals(result, address)
        }
}