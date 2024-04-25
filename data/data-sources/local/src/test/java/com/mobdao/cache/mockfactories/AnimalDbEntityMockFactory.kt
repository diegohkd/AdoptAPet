package com.mobdao.cache.mockfactories

import com.mobdao.cache.database.entities.Animal
import io.mockk.every
import io.mockk.mockk

internal object AnimalDbEntityMockFactory {

    fun create(
        id: String = "id",
    ): Animal = mockk {
        every { this@mockk.id } returns id
    }
}