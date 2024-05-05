package com.mobdao.local.mockfactories

import com.mobdao.local.internal.database.entities.Animal
import io.mockk.every
import io.mockk.mockk

internal object AnimalDbEntityMockFactory {

    fun create(
        id: String = "id",
    ): Animal = mockk {
        every { this@mockk.id } returns id
    }
}
