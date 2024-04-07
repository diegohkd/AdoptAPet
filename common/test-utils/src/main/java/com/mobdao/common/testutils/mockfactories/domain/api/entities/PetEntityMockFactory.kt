package com.mobdao.common.testutils.mockfactories.domain.api.entities

import com.mobdao.common.testutils.mockfactories.domain.BreedsMockFactory
import com.mobdao.common.testutils.mockfactories.domain.PhotoMockFactory
import com.mobdao.domain.api.entitites.Breeds
import com.mobdao.domain.api.entitites.Pet
import com.mobdao.domain.api.entitites.Photo
import io.mockk.every
import io.mockk.mockk

object PetEntityMockFactory {

    fun create(
        id: String = "id",
        name: String = "name",
        breeds: Breeds = BreedsEntityMockFactory.create(),
        photos: List<Photo> = listOf(PhotoEntityMockFactory.create()),
    ): Pet = mockk {
        every { this@mockk.id } returns id
        every { this@mockk.name } returns name
        every { this@mockk.breeds } returns breeds
        every { this@mockk.photos } returns photos
    }
}