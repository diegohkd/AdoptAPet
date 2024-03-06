package com.mobdao.domain.utils.mappers

import com.mobdao.domain.common_models.Pet
import com.mobdao.domain.common_models.Photo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetMapper @Inject constructor() {

    fun map(pet: PetEntity): Pet =
        Pet(
            id = pet.id,
            name = pet.name,
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