package com.mobdao.domain

import com.mobdao.domain_api.PetsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPetsUseCase @Inject constructor(
    private val petsRepository: PetsRepository,
) {

    data class Pet(
        val id: String,
        val name: String,
        val photos: List<Photo>,
    )

    data class Photo(
        val smallUrl: String,
        val mediumUrl: String,
        val largeUrl: String,
        val fullUrl: String,
    )

    fun execute(): Flow<List<Pet>> = flow {
        emit(
            petsRepository.getPets().map {
                Pet(
                    id = it.id,
                    name = it.name,
                    photos = it.photos.map {
                        Photo(
                            smallUrl = it.smallUrl,
                            mediumUrl = it.mediumUrl,
                            largeUrl = it.largeUrl,
                            fullUrl = it.fullUrl,
                        )
                    }
                )
            }
        )
    }
}