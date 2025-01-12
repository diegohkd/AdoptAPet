package com.mobdao.data.repositories

import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.adoptapet.domain.entities.GeoCoordinatesEntity
import com.mobdao.local.GeoLocationLocalDataSource
import com.mobdao.remote.GeoLocationRemoteDataSource
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GeoLocationRepositoryImplTest {
    private val currentGeoCoordinates: GeoCoordinatesEntity = mockk()
    private val locationAddress1: AddressEntity = mockk()
    private val locationAddress2: AddressEntity = mockk()
    private val addresses = listOf(locationAddress1, locationAddress2)

    private val geoLocationRemoteDataSource: GeoLocationRemoteDataSource =
        mockk {
            coEvery { getCurrentLocationCoordinates() } returns currentGeoCoordinates
            coEvery { getLocationAddress(currentGeoCoordinates) } returns addresses
            coEvery { autocompleteLocation("location") } returns addresses
        }
    private val geoLocationLocalDataSource: GeoLocationLocalDataSource =
        mockk {
            coEvery { getCurrentAddress() } returns locationAddress1
            coJustRun { saveCurrentAddress(locationAddress1) }
        }

    private val tested =
        GeoLocationRepositoryImpl(
            geoLocationRemoteDataSource = geoLocationRemoteDataSource,
            geoLocationLocalDataSource = geoLocationLocalDataSource,
        )

    @Test
    fun `given current geo coordinates returns empty list of reversed geocoding when get current location address then null is returned`() =
        runTest {
            // given
            coEvery { geoLocationRemoteDataSource.getLocationAddress(currentGeoCoordinates) } returns emptyList()

            // when
            val result: AddressEntity? = tested.getCurrentLocationAddress()

            // then
            assertNull(result)
        }

    @Test
    fun `given current geo coordinates returns non-empty list of reversed geocoding when get current location address then first address found is returned`() =
        runTest {
            // given / when
            val result: AddressEntity? = tested.getCurrentLocationAddress()

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
            val result: List<AddressEntity> = tested.autocompleteLocation(location = "")

            // then
            assertEquals(result, emptyList<AddressEntity>())
        }

    @Test
    fun `given location is not empty when autocomplete location then autocomplete options are returned`() =
        runTest {
            // given / when
            val result: List<AddressEntity> = tested.autocompleteLocation(location = "location")

            // then
            assertEquals(result, listOf(locationAddress1, locationAddress2))
        }

    @Test
    fun `given address when cache current location address then address is cached as current address`() =
        runTest {
            // given / when
            tested.cacheCurrentLocationAddress(address = locationAddress1)

            // then
            coVerify { geoLocationLocalDataSource.saveCurrentAddress(locationAddress1) }
        }
}
