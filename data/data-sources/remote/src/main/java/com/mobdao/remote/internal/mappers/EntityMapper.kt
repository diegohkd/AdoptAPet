package com.mobdao.remote.internal.mappers

import com.mobdao.domain.entities.Address
import com.mobdao.domain.entities.Pet
import com.mobdao.domain.entities.Photo
import com.mobdao.remote.internal.responses.Animal
import com.mobdao.remote.internal.responses.GeocodeResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EntityMapper @Inject constructor() {

    fun toPets(animals: List<Animal>): List<Pet> =
        animals.map { animal ->
            with(animal) {
                Pet(
                    id = id,
                    name = name,
                    breeds = com.mobdao.domain.entities.Breeds(
                        primary = breeds.primary,
                        secondary = breeds.secondary,
                    ),
                    photos = photos.map { photo ->
                        Photo(
                            smallUrl = photo.small,
                            mediumUrl = photo.medium,
                            largeUrl = photo.large,
                            fullUrl = photo.full,
                        )
                    }
                )
            }
        }

    fun toAddresses(geocodeResponse: GeocodeResponse): List<Address> =
        geocodeResponse.results.map { geocodeResult ->
            with (geocodeResult) {
                Address(
                    addressLine = formatted,
                    latitude = lat,
                    longitude = lon,
                )
            }
        }
}