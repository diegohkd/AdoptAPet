package com.mobdao.common.testutils.mockfactories.data.local

import com.mobdao.data.common.AnimalDbEntity
import io.mockk.every
import io.mockk.mockk

object AnimalDbEntityMockFactory {

    fun create(
        id: String = "id",
    ): AnimalDbEntity = mockk {
        every { this@mockk.id } returns id
    }
}