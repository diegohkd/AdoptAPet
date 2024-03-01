package com.mobdao.data

import com.mobdao.data.remote.services.PetFinderService
import com.mobdao.domain_api.PetsRepository
import com.mobdao.domain_api.entitites.Pet
import com.mobdao.domain_api.entitites.Photo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetsRepositoryImpl @Inject constructor(
    private val petFinderService: PetFinderService,
) : PetsRepository {

    override suspend fun getPets(): List<Pet> {
        return petFinderService.getAnimals().animals.map {
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
}