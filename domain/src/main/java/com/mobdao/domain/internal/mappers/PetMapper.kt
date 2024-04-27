package com.mobdao.domain.internal.mappers

import com.mobdao.domain.internal.AnimalTypeEntity
import com.mobdao.domain.internal.PetEntity
import com.mobdao.domain.models.AnimalType.*
import com.mobdao.domain.models.Breeds
import com.mobdao.domain.models.Pet
import com.mobdao.domain.models.Photo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PetMapper @Inject constructor() {

    fun map(pet: PetEntity): Pet =
        with(pet) {
            Pet(
                id = id,
                type = when (type) {
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
                breeds = Breeds(
                    primary = pet.breeds.primary,
                    secondary = pet.breeds.secondary,
                ),
                photos = pet.photos.map {
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
