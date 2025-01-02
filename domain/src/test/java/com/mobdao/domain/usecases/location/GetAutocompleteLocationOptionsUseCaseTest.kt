package com.mobdao.domain.usecases.location

import com.mobdao.adoptapet.domain.dataapi.repositories.GeoLocationRepository
import com.mobdao.adoptapet.domain.internal.AddressEntity
import com.mobdao.adoptapet.domain.internal.mappers.AddressMapper
import com.mobdao.adoptapet.domain.models.Address
import com.mobdao.adoptapet.domain.usecases.location.GetAutocompleteLocationOptionsUseCase
import com.mobdao.common.testutils.mockfactories.domain.AddressMockFactory
import com.mobdao.common.testutils.mockfactories.domain.entities.AddressEntityMockFactory
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAutocompleteLocationOptionsUseCaseTest {
    private val location: String = "location"
    private val autocompleteAddressEntity1: AddressEntity = AddressEntityMockFactory.create()
    private val autocompleteAddressEntity2: AddressEntity = AddressEntityMockFactory.create()

    private val autocompleteAddress1: Address = AddressMockFactory.create()
    private val autocompleteAddress2: Address = AddressMockFactory.create()

    private val geoLocationRepository: GeoLocationRepository =
        mockk {
            coEvery { autocompleteLocation(location) } returns
                listOf(
                    autocompleteAddressEntity1,
                    autocompleteAddressEntity2,
                )
        }
    private val addressMapper: AddressMapper =
        mockk {
            every { map(autocompleteAddressEntity1) } returns autocompleteAddress1
            every { map(autocompleteAddressEntity2) } returns autocompleteAddress2
        }

    private val tested =
        GetAutocompleteLocationOptionsUseCase(
            geoLocationRepository = geoLocationRepository,
            addressMapper = addressMapper,
        )

    @Test
    fun `given location when executed then autocomplete address options are returned`() =
        runTest {
            // given / when
            val result: List<Address> = tested.execute(location).first()

            // then
            assertEquals(result, listOf(autocompleteAddress1, autocompleteAddress2))
        }
}
