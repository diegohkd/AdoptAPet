package com.mobdao.data.utils

import com.mobdao.data.common.AnimalDbEntity
import com.mobdao.data.common.AnimalRemoteResponse
import com.mobdao.data.common.PhotoDbEntity
import com.mobdao.domain_api.entitites.Pet
import com.mobdao.domain_api.entitites.Photo
import com.mobdao.remote.responses.Animal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalMapper @Inject constructor() {

    fun mapToDbEntity(animals: List<AnimalRemoteResponse>): List<AnimalDbEntity> =
        animals.map { animal ->
            AnimalDbEntity(
                animal.id,
                animal.name,
                animal.photos.map {
                    PhotoDbEntity(
                        smallUrl = it.small,
                        mediumUrl = it.medium,
                        largeUrl = it.large,
                        fullUrl = it.full,
                    )
                }
            )
        }


    fun mapToPet(vararg animals: Animal): List<Pet> =
        animals.map {
            Pet(
                id = it.id,
                name = it.name,
                photos = it.photos.map {
                    Photo(
                        smallUrl = it.small,
                        mediumUrl = it.medium,
                        largeUrl = it.large,
                        fullUrl = it.full,
                    )
                }
            )
        }
}