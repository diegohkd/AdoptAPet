package com.mobdao.domain.internal.mappers

import com.mobdao.domain.internal.AnimalTypeEntity
import com.mobdao.domain.internal.PetEntity
import com.mobdao.domain.models.AnimalType.BARNYARD
import com.mobdao.domain.models.AnimalType.BIRD
import com.mobdao.domain.models.AnimalType.CAT
import com.mobdao.domain.models.AnimalType.DOG
import com.mobdao.domain.models.AnimalType.HORSE
import com.mobdao.domain.models.AnimalType.RABBIT
import com.mobdao.domain.models.AnimalType.SCALES_FINS_AND_OTHER
import com.mobdao.domain.models.AnimalType.SMALL_AND_FURRY
import com.mobdao.domain.models.Breeds
import com.mobdao.domain.models.Contact
import com.mobdao.domain.models.Pet
import com.mobdao.domain.models.Photo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PetMapper
    @Inject
    constructor() {
        fun map(pet: PetEntity): Pet =
            with(pet) {
                Pet(
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
                    name = pet.name,
                    breeds =
                        Breeds(
                            primary = pet.breeds.primary,
                            secondary = pet.breeds.secondary,
                        ),
                    age = age,
                    size = size,
                    gender = gender,
                    description = description,
                    distance = distance,
                    photos =
                        pet.photos.map {
                            Photo(
                                smallUrl = it.smallUrl,
                                mediumUrl = it.mediumUrl,
                                largeUrl = it.largeUrl,
                                fullUrl = it.fullUrl,
                            )
                        },
                    contact =
                        Contact(
                            email = contact?.email.orEmpty(),
                            phone = contact?.phone.orEmpty(),
                        ),
                )
            }
    }
