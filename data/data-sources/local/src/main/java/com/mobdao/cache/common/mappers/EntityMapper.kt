package com.mobdao.cache.common.mappers

import com.mobdao.cache.common.DomainEntityAddress
import com.mobdao.cache.common.DomainEntityBreeds
import com.mobdao.cache.common.DomainEntityPhoto
import com.mobdao.cache.database.entities.Address
import com.mobdao.cache.database.entities.Animal
import com.mobdao.cache.database.entities.Breeds
import com.mobdao.cache.database.entities.Photo
import com.mobdao.domain.entities.Pet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EntityMapper @Inject constructor() {

    fun toAnimal(pets: List<Pet>): List<Animal> =
        pets.map { pet ->
            with(pet) {
                Animal(
                    id = id,
                    name = name,
                    breeds = Breeds(
                        primaryBreed = breeds.primary,
                        secondaryBreed = breeds.secondary,
                    ),
                    photos.map {
                        Photo(
                            smallUrl = it.smallUrl,
                            mediumUrl = it.mediumUrl,
                            largeUrl = it.largeUrl,
                            fullUrl = it.fullUrl,
                        )
                    }
                )
            }
        }

    fun toPet(animal: Animal): Pet =
        with(animal) {
            Pet(
                id = id,
                name = name,
                breeds = DomainEntityBreeds(
                    primary = breeds.primaryBreed,
                    secondary = breeds.secondaryBreed,
                ),
                photos.map {
                    DomainEntityPhoto(
                        smallUrl = it.smallUrl,
                        mediumUrl = it.mediumUrl,
                        largeUrl = it.largeUrl,
                        fullUrl = it.fullUrl,
                    )
                }
            )
        }

    fun toDbEntity(address: DomainEntityAddress): Address =
        with(address) {
            Address(
                latitude = latitude,
                longitude = longitude,
                addressLine = addressLine,
            )
        }

    fun toDomainEntity(address: Address): DomainEntityAddress =
        with(address) {
            DomainEntityAddress(
                latitude = latitude,
                longitude = longitude,
                addressLine = addressLine,
            )
        }
}