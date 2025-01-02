package com.mobdao.remote

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.mobdao.adoptapet.common.config.AppConfig
import com.mobdao.adoptapet.domain.entities.Address
import com.mobdao.adoptapet.domain.entities.GeoCoordinates
import com.mobdao.remote.internal.services.GeoapifyService
import com.mobdao.remote.internal.utils.mappers.EntityMapper
import com.mobdao.remote.internal.utils.wrappers.FusedLocationProviderClientWrapper
import javax.inject.Inject
import javax.inject.Singleton

internal const val REVERSE_GEOCODE_RESPONSE_FORMAT = "json"

@Singleton
class GeoLocationRemoteDataSource
    @Inject
    internal constructor(
        private val fusedLocationClient: FusedLocationProviderClientWrapper,
        private val geoapifyService: GeoapifyService,
        private val appConfig: AppConfig,
        private val entityMapper: EntityMapper,
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

        suspend fun getLocationAddress(geoCoordinates: GeoCoordinates): List<Address> =
            geoapifyService
                .reverseGeocode(
                    apiKey = geoapifyKey,
                    latitude = geoCoordinates.latitude,
                    longitude = geoCoordinates.longitude,
                    format = REVERSE_GEOCODE_RESPONSE_FORMAT,
                ).let(entityMapper::toAddresses)

        suspend fun autocompleteLocation(location: String): List<Address> =
            geoapifyService
                .autocomplete(
                    apiKey = geoapifyKey,
                    text = location,
                    format = REVERSE_GEOCODE_RESPONSE_FORMAT,
                ).let(entityMapper::toAddresses)
    }
