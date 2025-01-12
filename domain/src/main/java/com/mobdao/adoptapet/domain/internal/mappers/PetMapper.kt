package com.mobdao.adoptapet.domain.internal.mappers

import com.mobdao.adoptapet.domain.entities.AnimalTypeEntity
import com.mobdao.adoptapet.domain.entities.PetEntity
import com.mobdao.adoptapet.domain.models.AnimalType.BARNYARD
import com.mobdao.adoptapet.domain.models.AnimalType.BIRD
import com.mobdao.adoptapet.domain.models.AnimalType.CAT
import com.mobdao.adoptapet.domain.models.AnimalType.DOG
import com.mobdao.adoptapet.domain.models.AnimalType.HORSE
import com.mobdao.adoptapet.domain.models.AnimalType.RABBIT
import com.mobdao.adoptapet.domain.models.AnimalType.SCALES_FINS_AND_OTHER
import com.mobdao.adoptapet.domain.models.AnimalType.SMALL_AND_FURRY
import com.mobdao.adoptapet.domain.models.Breeds
import com.mobdao.adoptapet.domain.models.Contact
import com.mobdao.adoptapet.domain.models.Pet
import com.mobdao.adoptapet.domain.models.Photo
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
