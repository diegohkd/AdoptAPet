package com.mobdao.common.testutils.mockfactories.domain

import com.mobdao.adoptapet.domain.models.Breeds
import io.mockk.every
import io.mockk.mockk

object BreedsMockFactory {
    fun create(
        primary: String? = "primary",
        secondary: String? = "secondary",
    ): Breeds =
        mockk {
            every { this@mockk.primary } returns primary
            every { this@mockk.secondary } returns secondary
        }
}
