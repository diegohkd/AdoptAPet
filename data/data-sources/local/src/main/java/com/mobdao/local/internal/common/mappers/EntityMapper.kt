package com.mobdao.local.internal.common.mappers

import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.adoptapet.domain.entities.AnimalTypeEntity
import com.mobdao.adoptapet.domain.entities.BreedsEntity
import com.mobdao.adoptapet.domain.entities.ContactEntity
import com.mobdao.adoptapet.domain.entities.PetEntity
import com.mobdao.adoptapet.domain.entities.PhotoEntity
import com.mobdao.local.internal.database.entities.AddressDbModel
import com.mobdao.local.internal.database.entities.AnimalDbModel
import com.mobdao.local.internal.database.entities.AnimalTypeDbModel.BARNYARD
import com.mobdao.local.internal.database.entities.AnimalTypeDbModel.BIRD
import com.mobdao.local.internal.database.entities.AnimalTypeDbModel.CAT
import com.mobdao.local.internal.database.entities.AnimalTypeDbModel.DOG
import com.mobdao.local.internal.database.entities.AnimalTypeDbModel.HORSE
import com.mobdao.local.internal.database.entities.AnimalTypeDbModel.RABBIT
import com.mobdao.local.internal.database.entities.AnimalTypeDbModel.SCALES_FINS_AND_OTHER
import com.mobdao.local.internal.database.entities.AnimalTypeDbModel.SMALL_AND_FURRY
import com.mobdao.local.internal.database.entities.BreedsDbModel
import com.mobdao.local.internal.database.entities.ContactDbModel
import com.mobdao.local.internal.database.entities.PhotoDbModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EntityMapper @Inject constructor() {
    fun toDbModel(pets: List<PetEntity>): List<AnimalDbModel> =
        pets.map { pet ->
            with(pet) {
                AnimalDbModel(
                    id = id,
                    type =
                        when (type) {
                            AnimalTypeEntity.DOG -> DOG
                            AnimalTypeEntity.CAT -> CAT
                            AnimalTypeEntity.RABBIT -> RABBIT
                            AnimalTypeEntity.SMALL_AND_FURRY -> SMALL_AND_FURRY
                            AnimalTypeEntity.HORSE -> HORSE
                            AnimalTypeEntity.BIRD -> BIRD
                            AnimalTypeEntity.SCALES_FINS_AND_OTHER -> SCALES_FINS_AND_OTHER
                            AnimalTypeEntity.BARNYARD -> BARNYARD
                        },
                    name = name,
                    breeds =
                        BreedsDbModel(
                            primaryBreed = breeds.primary,
                            secondaryBreed = breeds.secondary,
                        ),
                    age = age,
                    size = size,
                    gender = gender,
                    description = description,
                    distance = distance,
                    photos =
                        photos.map {
                            PhotoDbModel(
                                smallUrl = it.smallUrl,
                                mediumUrl = it.mediumUrl,
                                largeUrl = it.largeUrl,
                                fullUrl = it.fullUrl,
                            )
                        },
                    contact =
                        ContactDbModel(
                            email = contact?.email.orEmpty(),
                            phone = contact?.phone.orEmpty(),
                        ),
                )
            }
        }

    fun toEntity(animal: AnimalDbModel): PetEntity =
        with(animal) {
            PetEntity(
                id = id,
                type =
                    when (type) {
                        DOG -> AnimalTypeEntity.DOG
                        CAT -> AnimalTypeEntity.CAT
                        RABBIT -> AnimalTypeEntity.RABBIT
                        SMALL_AND_FURRY -> AnimalTypeEntity.SMALL_AND_FURRY
                        HORSE -> AnimalTypeEntity.HORSE
                        BIRD -> AnimalTypeEntity.BIRD
                        SCALES_FINS_AND_OTHER -> AnimalTypeEntity.SCALES_FINS_AND_OTHER
                        BARNYARD -> AnimalTypeEntity.BARNYARD
                    },
                name = name,
                breeds =
                    BreedsEntity(
                        primary = breeds.primaryBreed,
                        secondary = breeds.secondaryBreed,
                    ),
                age = age,
                size = size,
                gender = gender,
                description = description,
                distance = distance,
                photos =
                    photos.map {
                        PhotoEntity(
                            smallUrl = it.smallUrl,
                            mediumUrl = it.mediumUrl,
                            largeUrl = it.largeUrl,
                            fullUrl = it.fullUrl,
                        )
                    },
                contact =
                    ContactEntity(
                        email = contact?.email.orEmpty(),
                        phone = contact?.phone.orEmpty(),
                    ),
            )
        }

    fun toDbModel(address: AddressEntity): AddressDbModel =
        with(address) {
            AddressDbModel(
                latitude = latitude,
                longitude = longitude,
                addressLine = addressLine,
            )
        }

    fun toEntity(address: AddressDbModel): AddressEntity =
        with(address) {
            AddressEntity(
                latitude = latitude,
                longitude = longitude,
                addressLine = addressLine,
            )
        }
}
