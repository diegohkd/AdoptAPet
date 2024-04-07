package com.mobdao.common.testutils.mockfactories.domain.api.entities

import com.mobdao.domain.api.entitites.Photo
import io.mockk.every
import io.mockk.mockk

object PhotoEntityMockFactory {

    fun create(
        smallUrl: String = "smallUrl",
        mediumUrl: String = "mediumUrl",
        largeUrl: String = "largeUrl",
        fullUrl: String = "fullUrl",
    ): Photo = mockk {
        every { this@mockk.smallUrl } returns smallUrl
        every { this@mockk.mediumUrl } returns mediumUrl
        every { this@mockk.largeUrl } returns largeUrl
        every { this@mockk.fullUrl } returns fullUrl
    }
}