package com.mobdao.adoptapet.common.testutils.domain.entities

import com.mobdao.adoptapet.domain.entities.BreedsEntity
import com.mobdao.adoptapet.domain.entities.PetEntity
import com.mobdao.adoptapet.domain.entities.PhotoEntity
import io.mockk.every
import io.mockk.mockk

object PetEntityMockFactory {
    fun create(
        id: String = "id",
        name: String = "name",
        breeds: BreedsEntity = BreedsEntityMockFactory.create(),
        photos: List<PhotoEntity> = listOf(PhotoEntityMockFactory.create()),
    ): PetEntity =
        mockk {
            every { this@mockk.id } returns id
            every { this@mockk.name } returns name
            every { this@mockk.breeds } returns breeds
            every { this@mockk.photos } returns photos
        }
}
