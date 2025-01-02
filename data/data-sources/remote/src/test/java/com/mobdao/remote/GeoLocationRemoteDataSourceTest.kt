package com.mobdao.remote

import android.location.Location
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.mobdao.adoptapet.common.config.AppConfig
import com.mobdao.adoptapet.common.config.GeoapifyConfig
import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.adoptapet.domain.entities.GeoCoordinatesEntity
import com.mobdao.remote.internal.responses.GeocodeResponse
import com.mobdao.remote.internal.services.GeoapifyService
import com.mobdao.remote.internal.utils.mappers.EntityMapper
import com.mobdao.remote.internal.utils.wrappers.FusedLocationProviderClientWrapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GeoLocationRemoteDataSourceTest {
    private val location: Location =
        mockk {
            every { latitude } returns -123.0
            every { longitude } returns 345.0
        }
    private val geoapifyConfig: GeoapifyConfig =
        mockk {
            every { apiKey } returns "apiKey"
        }
    private val geocodeResponse: GeocodeResponse = mockk()
    private val address1: AddressEntity = mockk()
    private val address2: AddressEntity = mockk()
    private val addresses = listOf(address1, address2)

    private val fusedLocationClient: FusedLocationProviderClientWrapper =
        mockk {
            coEvery { getCurrentLocation(PRIORITY_HIGH_ACCURACY) } returns location
        }
    private val geoapifyService: GeoapifyService =
        mockk {
            coEvery {
                reverseGeocode(
                    apiKey = "apiKey",
                    latitude = -123.0,
                    longitude = 456.0,
                    format = REVERSE_GEOCODE_RESPONSE_FORMAT,
                )
            } returns geocodeResponse
            coEvery {
                autocomplete(
                    apiKey = "apiKey",
                    text = "location",
                    format = REVERSE_GEOCODE_RESPONSE_FORMAT,
                )
            } returns geocodeResponse
        }
    private val appConfig: AppConfig =
        mockk {
            every { geoapifyConfig } returns this@GeoLocationRemoteDataSourceTest.geoapifyConfig
        }
    private val entityMapper: EntityMapper =
        mockk {
            every { toAddresses(geocodeResponse) } returns addresses
        }

    private val tested =
        GeoLocationRemoteDataSource(
            fusedLocationClient = fusedLocationClient,
            geoapifyService = geoapifyService,
            appConfig = appConfig,
            entityMapper = entityMapper,
        )

    @Test
    fun `when get current location coordinates then current location coordinates is returned`() =
        runTest {
            // when
            val result: GeoCoordinatesEntity = tested.getCurrentLocationCoordinates()

            // then
            assertEquals(
                result,
                GeoCoordinatesEntity(
                    latitude = -123.0,
                    longitude = 345.0,
                ),
            )
        }

    @Test
    fun `given geo coordinates when get location address then reverse geocode is applied and matching addresses are returned`() =
        runTest {
            // given
            val geoCoordinates = GeoCoordinatesEntity(latitude = -123.0, longitude = 456.0)

            // when
            val result: List<AddressEntity> = tested.getLocationAddress(geoCoordinates = geoCoordinates)

            // then
            assertEquals(
                result,
                addresses,
            )
        }

    @Test
    fun `given location query when get autocomplete address then autocomplete options are fetched and matching addresses are returned`() =
        runTest {
            // when
            val result: List<AddressEntity> = tested.autocompleteLocation(location = "location")

            // then
            assertEquals(
                result,
                addresses,
            )
        }
}
