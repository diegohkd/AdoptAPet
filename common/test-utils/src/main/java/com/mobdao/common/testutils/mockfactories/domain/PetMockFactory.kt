package com.mobdao.common.testutils.mockfactories.domain

import com.mobdao.domain.models.Breeds
import com.mobdao.domain.models.Pet
import com.mobdao.domain.models.Photo
import io.mockk.every
import io.mockk.mockk

object PetMockFactory {

    fun create(
        id: String = "id",
        name: String = "name",
        breeds: Breeds = BreedsMockFactory.create(),
        photos: List<Photo> = listOf(PhotoMockFactory.create()),
    ): Pet = mockk {
        every { this@mockk.id } returns id
        every { this@mockk.name } returns name
        every { this@mockk.breeds } returns breeds
        every { this@mockk.photos } returns photos
    }
}