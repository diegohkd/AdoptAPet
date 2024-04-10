package com.mobdao.remote

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.mobdao.common.config.AppConfig
import com.mobdao.remote.responses.GeoCoordinates
import com.mobdao.remote.responses.GeocodeResponse
import com.mobdao.remote.services.GeoapifyService
import com.mobdao.remote.utils.wrappers.FusedLocationProviderClientWrapper
import javax.inject.Inject
import javax.inject.Singleton

internal const val REVERSE_GEOCODE_RESPONSE_FORMAT = "json"

@Singleton
class GeoLocationRemoteDataSource @Inject internal constructor(
    private val fusedLocationClient: FusedLocationProviderClientWrapper,
    private val geoapifyService: GeoapifyService,
    private val appConfig: AppConfig,
) {

    private val geoapifyKey: String by lazy { appConfig.geoapifyConfig.apiKey }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocationCoordinates(): GeoCoordinates {
        val location: Location = fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY)
        return GeoCoordinates(
            latitude = location.latitude,
            longitude = location.longitude,
        )
    }

    suspend fun getLocationAddress(geoCoordinates: GeoCoordinates): GeocodeResponse =
        geoapifyService.reverseGeocode(
            apiKey = geoapifyKey,
            latitude = geoCoordinates.latitude,
            longitude = geoCoordinates.longitude,
            format = REVERSE_GEOCODE_RESPONSE_FORMAT
        )

    suspend fun autocompleteLocation(location: String): GeocodeResponse =
        geoapifyService.autocomplete(
            apiKey = geoapifyKey,
            text = location,
            format = REVERSE_GEOCODE_RESPONSE_FORMAT
        )
}