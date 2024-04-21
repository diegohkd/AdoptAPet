package com.mobdao.data.utils.mappers

import com.mobdao.data.common.AnimalDbEntity
import com.mobdao.data.common.AnimalRemoteResponse
import com.mobdao.data.common.BreedsDbEntity
import com.mobdao.data.common.PhotoDbEntity
import com.mobdao.domain.dataapi.entitites.Breeds
import com.mobdao.domain.dataapi.entitites.Pet
import com.mobdao.domain.dataapi.entitites.Photo
import com.mobdao.remote.responses.Animal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalMapper @Inject constructor() {

    fun mapToDbEntity(animals: List<AnimalRemoteResponse>): List<AnimalDbEntity> =
        animals.map { animal ->
            AnimalDbEntity(
                id = animal.id,
                name = animal.name,
                breeds = BreedsDbEntity(
                    primaryBreed = animal.breeds.primary,
                    secondaryBreed = animal.breeds.secondary,
                ),
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

    fun mapToPet(animals: List<Animal>): List<Pet> =
        animals.map {
            Pet(
                id = it.id,
                name = it.name,
                breeds = Breeds(
                    primary = it.breeds.primary,
                    secondary = it.breeds.secondary,
                ),
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

    fun mapToPet(animal: AnimalDbEntity): Pet =
        Pet(
            id = animal.id,
            name = animal.name,
            breeds = Breeds(
                primary = animal.breeds.primaryBreed,
                secondary = animal.breeds.secondaryBreed,
            ),
            photos = animal.photos.map {
                Photo(
                    smallUrl = it.smallUrl,
                    mediumUrl = it.mediumUrl,
                    largeUrl = it.largeUrl,
                    fullUrl = it.fullUrl,
                )
            }
        )
}
