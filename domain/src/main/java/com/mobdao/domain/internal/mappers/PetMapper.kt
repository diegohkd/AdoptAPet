package com.mobdao.domain.internal.mappers

import com.mobdao.domain.models.Breeds
import com.mobdao.domain.models.Pet
import com.mobdao.domain.models.Photo
import com.mobdao.domain.internal.PetEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PetMapper @Inject constructor() {

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
