package com.mobdao.remote

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import com.mobdao.common.config.AppConfig
import com.mobdao.remote.responses.GeoCoordinates
import com.mobdao.remote.responses.GeocodeResponse
import com.mobdao.remote.services.GeoapifyService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val REVERSE_GEOCODE_RESPONSE_FORMAT = "json"

@Singleton
class GeoLocationRemoteDataSource @Inject internal constructor(
    private val geoapifyService: GeoapifyService,
    context: Context,
    private val appConfig: AppConfig,
) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocationCoordinates(): GeoCoordinates {
        val location = fusedLocationClient.getCurrentLocation(
            PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).await()
        return GeoCoordinates(
            latitude = location.latitude,
            longitude = location.longitude,
        )
    }

    suspend fun getLocationAddress(geoCoordinates: GeoCoordinates): GeocodeResponse =
        geoapifyService.reverseGeocode(
            apiKey = appConfig.geoapifyConfig.apiKey,
            latitude = geoCoordinates.latitude,
            longitude = geoCoordinates.longitude,
            format = REVERSE_GEOCODE_RESPONSE_FORMAT
        )

    suspend fun autocompleteLocation(location: String): GeocodeResponse =
        geoapifyService.autocomplete(
            apiKey = appConfig.geoapifyConfig.apiKey,
            text = location,
            format = REVERSE_GEOCODE_RESPONSE_FORMAT
        )
}
