package com.mobdao.local.mockfactories

import com.mobdao.local.internal.database.entities.AnimalDbModel
import io.mockk.every
import io.mockk.mockk

internal object AnimalDbEntityMockFactory {
    fun create(id: String = "id"): AnimalDbModel =
        mockk {
            every { this@mockk.id } returns id
        }
}
