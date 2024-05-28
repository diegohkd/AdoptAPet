package com.mobdao.local.internal.common.mappers

import com.mobdao.domain.entities.Pet
import com.mobdao.local.internal.common.*
import com.mobdao.local.internal.database.entities.*
import com.mobdao.local.internal.database.entities.AnimalType.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EntityMapper @Inject constructor() {

    fun toAnimal(pets: List<Pet>): List<Animal> =
        pets.map { pet ->
            with(pet) {
                Animal(
                    id = id,
                    type = when (type) {
                        DomainEntityAnimalType.DOG -> DOG
                        DomainEntityAnimalType.CAT -> CAT
                        DomainEntityAnimalType.RABBIT -> RABBIT
                        DomainEntityAnimalType.SMALL_AND_FURRY -> SMALL_AND_FURRY
                        DomainEntityAnimalType.HORSE -> HORSE
                        DomainEntityAnimalType.BIRD -> BIRD
                        DomainEntityAnimalType.SCALES_FINS_AND_OTHER -> SCALES_FINS_AND_OTHER
                        DomainEntityAnimalType.BARNYARD -> BARNYARD
                    },
                    name = name,
                    breeds = Breeds(
                        primaryBreed = breeds.primary,
                        secondaryBreed = breeds.secondary,
                    ),
                    age = age,
                    size = size,
                    gender = gender,
                    description = description,
                    distance = distance,
                    photos = photos.map {
                        Photo(
                            smallUrl = it.smallUrl,
                            mediumUrl = it.mediumUrl,
                            largeUrl = it.largeUrl,
                            fullUrl = it.fullUrl,
                        )
                    },
                    contact = Contact(
                        email = contact?.email.orEmpty(),
                        phone = contact?.phone.orEmpty()
                    )
                )
            }
        }

    fun toPet(animal: Animal): Pet =
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
                breeds = DomainEntityBreeds(
                    primary = breeds.primaryBreed,
                    secondary = breeds.secondaryBreed,
                ),
                age = age,
                size = size,
                gender = gender,
                description = description,
                distance = distance,
                photos = photos.map {
                    DomainEntityPhoto(
                        smallUrl = it.smallUrl,
                        mediumUrl = it.mediumUrl,
                        largeUrl = it.largeUrl,
                        fullUrl = it.fullUrl,
                    )
                },
                contact = DomainEntityContact(
                    email = contact?.email.orEmpty(),
                    phone = contact?.phone.orEmpty(),
                ),
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
