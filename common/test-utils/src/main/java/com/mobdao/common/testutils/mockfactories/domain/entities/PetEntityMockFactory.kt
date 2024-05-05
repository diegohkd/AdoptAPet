package com.mobdao.common.testutils.mockfactories.domain.entities

import io.mockk.every
import io.mockk.mockk

object PetEntityMockFactory {

    fun create(
        id: String = "id",
        name: String = "name",
        breeds: com.mobdao.domain.entities.Breeds = BreedsEntityMockFactory.create(),
        photos: List<com.mobdao.domain.entities.Photo> = listOf(PhotoEntityMockFactory.create()),
    ): com.mobdao.domain.entities.Pet = mockk {
        every { this@mockk.id } returns id
        every { this@mockk.name } returns name
        every { this@mockk.breeds } returns breeds
        every { this@mockk.photos } returns photos
    }
}
