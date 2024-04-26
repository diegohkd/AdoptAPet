package com.mobdao.remote.internal.wrappers

import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FusedLocationProviderClientWrapper @Inject constructor(context: Context) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(
        anyOf = [
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        ]
    )
    suspend fun getCurrentLocation(priority: Int): Location =
        fusedLocationClient.getCurrentLocation(
            priority,
            CancellationTokenSource().token,
        ).await()
}