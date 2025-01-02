package com.mobdao.common.testutils.mockfactories.domain.entities

import com.mobdao.adoptapet.domain.entities.Breeds
import com.mobdao.adoptapet.domain.entities.Pet
import com.mobdao.adoptapet.domain.entities.Photo
import io.mockk.every
import io.mockk.mockk

object PetEntityMockFactory {
    fun create(
        id: String = "id",
        name: String = "name",
        breeds: Breeds = BreedsEntityMockFactory.create(),
        photos: List<Photo> = listOf(PhotoEntityMockFactory.create()),
    ): Pet =
        mockk {
            every { this@mockk.id } returns id
            every { this@mockk.name } returns name
            every { this@mockk.breeds } returns breeds
            every { this@mockk.photos } returns photos
        }
}
