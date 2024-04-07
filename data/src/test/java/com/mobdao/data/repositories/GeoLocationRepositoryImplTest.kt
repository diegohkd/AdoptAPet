package com.mobdao.data.repositories

import com.mobdao.cache.GeoLocationLocalDataSource
import com.mobdao.data.common.AddressDbEntity
import com.mobdao.data.utils.mappers.AddressMapper
import com.mobdao.domain.api.entitites.Address
import com.mobdao.remote.GeoLocationRemoteDataSource
import com.mobdao.remote.responses.GeoCoordinates
import com.mobdao.remote.responses.GeocodeResponse
import com.mobdao.remote.responses.GeocodeResult
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GeoLocationRepositoryImplTest {

    private val currentGeoCoordinates: GeoCoordinates = mockk()
    private val locationGeocodeResult1: GeocodeResult = mockk()
    private val locationGeocodeResult2: GeocodeResult = mockk()
    private val locationAddress1: Address = mockk()
    private val locationAddress2: Address = mockk()
    private val locationGeocodeResponse: GeocodeResponse = mockk {
        every { results } returns listOf(
            locationGeocodeResult1,
            locationGeocodeResult2
        )
    }
    private val currentAddressDbEntity: AddressDbEntity = mockk()

    private val geoLocationRemoteDataSource: GeoLocationRemoteDataSource = mockk {
        coEvery { getCurrentLocationCoordinates() } returns currentGeoCoordinates
        coEvery { getLocationAddress(currentGeoCoordinates) } returns locationGeocodeResponse
        coEvery { autocompleteLocation("location") } returns locationGeocodeResponse
    }
    private val geoLocationLocalDataSource: GeoLocationLocalDataSource = mockk {
        coEvery { getCurrentAddress() } returns currentAddressDbEntity
        coJustRun { saveCurrentAddress(currentAddressDbEntity) }
    }
    private val addressMapper: AddressMapper = mockk {
        every { mapToEntity(locationGeocodeResult1) } returns locationAddress1
        every { mapToEntity(locationGeocodeResult2) } returns locationAddress2
        every { mapToEntity(currentAddressDbEntity) } returns locationAddress1
        every { mapToDbEntity(locationAddress1) } returns currentAddressDbEntity
    }

    private val tested = GeoLocationRepositoryImpl(
        geoLocationRemoteDataSource = geoLocationRemoteDataSource,
        geoLocationLocalDataSource = geoLocationLocalDataSource,
        addressMapper = addressMapper,
    )

    @Test
    fun `given current geo coordinates returns empty list of reversed geocoding when get current location address then null is returned`() =
        runTest {
            // given
            every { locationGeocodeResponse.results } returns emptyList()

            // when
            val result: Address? = tested.getCurrentLocationAddress()

            // then
            assertNull(result)
        }

    @Test
    fun `given current geo coordinates returns non-empty list of reversed geocoding when get current location address then first address found is returned`() =
        runTest {
            // given / when
            val result: Address? = tested.getCurrentLocationAddress()

            // then
            assertEquals(result, locationAddress1)
        }

    @Test
    fun `when get cached current location address then cached current location address is returned`() =
        runTest {
            // when
            val result = tested.getCachedCurrentLocationAddress()

            // then
            assertEquals(result, locationAddress1)
        }

    @Test
    fun `given location is empty when autocomplete location then empty list is returned`() =
        runTest {
            // given / when
            val result: List<Address> = tested.autocompleteLocation(location = "")

            // then
            assertEquals(result, emptyList<Address>())
        }

    @Test
    fun `given location is not empty when autocomplete location then autocomplete options are returned`() =
        runTest {
            // given / when
            val result: List<Address> = tested.autocompleteLocation(location = "location")

            // then
            assertEquals(result, listOf(locationAddress1, locationAddress2))
        }

    @Test
    fun `given address when cache current location address then address is cached as current address`() =
        runTest {
            // given / when
            tested.cacheCurrentLocationAddress(address = locationAddress1)

            // then
            coVerify { geoLocationLocalDataSource.saveCurrentAddress(currentAddressDbEntity) }
        }
}