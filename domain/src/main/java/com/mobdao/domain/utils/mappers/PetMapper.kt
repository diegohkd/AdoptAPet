package com.mobdao.domain.utils.mappers

import com.mobdao.domain.models.Breeds
import com.mobdao.domain.models.Pet
import com.mobdao.domain.models.Photo
import com.mobdao.domain.utils.PetEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetMapper @Inject constructor() {

    fun map(pet: PetEntity): Pet =
        Pet(
            id = pet.id,
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
