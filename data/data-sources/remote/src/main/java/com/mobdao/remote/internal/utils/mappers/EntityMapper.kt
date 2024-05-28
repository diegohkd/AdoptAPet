package com.mobdao.remote.internal.utils.mappers

import com.mobdao.domain.entities.Address
import com.mobdao.domain.entities.Pet
import com.mobdao.domain.entities.Photo
import com.mobdao.remote.internal.responses.Animal
import com.mobdao.remote.internal.responses.AnimalType.*
import com.mobdao.remote.internal.responses.GeocodeResponse
import com.mobdao.remote.internal.utils.DomainEntityAnimalType
import com.mobdao.remote.internal.utils.DomainEntityContact
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EntityMapper @Inject constructor() {

    fun toPets(animals: List<Animal>): List<Pet> =
        animals.map { animal ->
            with(animal) {
                Pet(
                    id = id,
                    type = when (type) {
                        DOG -> DomainEntityAnimalType.DOG
                        CAT -> DomainEntityAnimalType.CAT
                        RABBIT -> DomainEntityAnimalType.RABBIT
                        SMALL_AND_FURRY -> DomainEntityAnimalType.SMALL_AND_FURRY
                        HORSE -> DomainEntityAnimalType.HORSE
                        BIRD -> DomainEntityAnimalType.BIRD
                        SCALES_FINS_AND_OTHER -> DomainEntityAnimalType.SCALES_FINS_AND_OTHER
                        BARNYARD -> DomainEntityAnimalType.BARNYARD
                    },
                    name = name,
                    breeds = com.mobdao.domain.entities.Breeds(
                        primary = breeds.primary,
                        secondary = breeds.secondary,
                    ),
                    age = age.orEmpty(),
                    size = size.orEmpty(),
                    gender = gender.orEmpty(),
                    description = description.orEmpty(),
                    distance = distance,
                    photos = photos.map { photo ->
                        Photo(
                            smallUrl = photo.small,
                            mediumUrl = photo.medium,
                            largeUrl = photo.large,
                            fullUrl = photo.full,
                        )
                    },
                    contact = DomainEntityContact(
                        email = contact?.email.orEmpty(),
                        phone = contact?.phone.orEmpty(),
                    )
                )
            }
        }

    fun toAddresses(geocodeResponse: GeocodeResponse): List<Address> =
        geocodeResponse.results.map { geocodeResult ->
            with(geocodeResult) {
                Address(
                    addressLine = formatted,
                    latitude = lat,
                    longitude = lon,
                )
            }
        }
}
