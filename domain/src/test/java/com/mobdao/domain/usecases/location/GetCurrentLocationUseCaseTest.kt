package com.mobdao.domain.usecases.location

import com.mobdao.adoptapet.common.exceptions.CurrentLocationNotFoundException
import com.mobdao.adoptapet.common.testutils.domain.AddressMockFactory
import com.mobdao.adoptapet.common.testutils.domain.entities.AddressEntityMockFactory
import com.mobdao.adoptapet.domain.dataapi.repositories.GeoLocationRepository
import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.adoptapet.domain.internal.mappers.AddressMapper
import com.mobdao.adoptapet.domain.models.Address
import com.mobdao.adoptapet.domain.usecases.location.GetCurrentLocationUseCase
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

    private val geoLocationRepository: GeoLocationRepository =
        mockk {
            coEvery { getCurrentLocationAddress() } returns addressEntity
        }
    private val addressMapper: AddressMapper =
        mockk {
            every { map(addressEntity) } returns address
        }

    private val tested =
        GetCurrentLocationUseCase(
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
