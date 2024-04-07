package com.mobdao.common.testutils.mockfactories.domain.api.entities

import com.mobdao.domain.api.entitites.Breeds
import io.mockk.every
import io.mockk.mockk

object BreedsEntityMockFactory {

    fun create(
        primary: String? = "primary",
        secondary: String? = "secondary",
    ): Breeds = mockk {
        every { this@mockk.primary } returns primary
        every { this@mockk.secondary } returns secondary
    }
}