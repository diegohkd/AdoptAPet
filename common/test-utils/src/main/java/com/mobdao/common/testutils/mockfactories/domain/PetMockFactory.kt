package com.mobdao.common.testutils.mockfactories.domain

import com.mobdao.adoptapet.domain.models.AnimalType
import com.mobdao.adoptapet.domain.models.Breeds
import com.mobdao.adoptapet.domain.models.Contact
import com.mobdao.adoptapet.domain.models.Pet
import com.mobdao.adoptapet.domain.models.Photo
import io.mockk.every
import io.mockk.mockk

object PetMockFactory {
    fun create(
        id: String = "id",
        type: AnimalType = AnimalType.DOG,
        name: String = "name",
        breeds: Breeds = BreedsMockFactory.create(),
        age: String = "age",
        size: String = "size",
        gender: String = "gender",
        description: String = "description",
        distance: Float = 123f,
        photos: List<Photo> = listOf(PhotoMockFactory.create()),
        contact: Contact = ContactMockFactory.create(),
    ): Pet =
        mockk {
            every { this@mockk.id } returns id
            every { this@mockk.type } returns type
            every { this@mockk.name } returns name
            every { this@mockk.breeds } returns breeds
            every { this@mockk.age } returns age
            every { this@mockk.size } returns size
            every { this@mockk.gender } returns gender
            every { this@mockk.description } returns description
            every { this@mockk.distance } returns distance
            every { this@mockk.photos } returns photos
            every { this@mockk.contact } returns contact
        }
}
