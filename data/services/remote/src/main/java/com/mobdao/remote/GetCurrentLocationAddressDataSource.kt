package com.mobdao.remote

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import com.mobdao.remote.responses.Address
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val MAX_ADDRESS_RESULTS = 1

// TODO is this a good name? Should it have the 'Get'?
@Singleton
class GetCurrentLocationAddressDataSource @Inject constructor(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Address? {
        val location = fusedLocationClient.getCurrentLocation(
            PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).await()
        return getAddress(location.latitude, location.longitude, context)
    }

    private suspend fun getAddress(
        latitude: Double,
        longitude: Double,
        context: Context
    ): Address? {
        val geocoder = Geocoder(context, Locale.getDefault())

        val address = try {
            if (Build.VERSION.SDK_INT >= TIRAMISU) {
                geocoder.getAddressFromLocation(latitude = latitude, longitude = longitude)
            } else {
                geocoder.getFromLocation(latitude, longitude, MAX_ADDRESS_RESULTS)?.firstOrNull()
            }
        } catch (e: IOException) {
            null
        }
        return address?.let {
            Address(
                addressLine = it.getAddressLine(0),
                latitude = it.latitude,
                longitude = it.longitude,
            )
        }
    }

    @TargetApi(TIRAMISU)
    private suspend fun Geocoder.getAddressFromLocation(
        latitude: Double,
        longitude: Double,
    ): android.location.Address? = suspendCoroutine { continuation ->
        getFromLocation(
            latitude,
            longitude,
            MAX_ADDRESS_RESULTS,
            object : GeocodeListener {
                override fun onGeocode(addresses: MutableList<android.location.Address>) {
                    continuation.resume(addresses.firstOrNull())
                }

                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                    continuation.resumeWithException(RuntimeException(errorMessage))
                }
            }
        )
    }
}