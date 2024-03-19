package com.mobdao.remote

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import com.mobdao.common.config.AppConfig
import com.mobdao.remote.responses.GeoCoordinates
import com.mobdao.remote.responses.ReverseGeocodeResponse
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
        // TODO remove this
        if (appConfig.isDebugBuild) {
            return GeoCoordinates(
                latitude = 40.74852484177265,
                longitude = -73.98573148311264,
            )
        }
        val location = fusedLocationClient.getCurrentLocation(
            PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).await()
        return GeoCoordinates(
            latitude = location.latitude,
            longitude = location.longitude,
        )
    }

    suspend fun getLocationAddress(geoCoordinates: GeoCoordinates): ReverseGeocodeResponse =
        geoapifyService.reverseGeocode(
            apiKey = appConfig.geoapifyConfig.apiKey,
            latitude = geoCoordinates.latitude,
            longitude = geoCoordinates.longitude,
            format = REVERSE_GEOCODE_RESPONSE_FORMAT
        )
}