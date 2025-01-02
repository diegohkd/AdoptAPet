package com.mobdao.adoptapet.common.testutils.domain.entities

import com.mobdao.adoptapet.domain.entities.PhotoEntity
import io.mockk.every
import io.mockk.mockk

object PhotoEntityMockFactory {
    fun create(
        smallUrl: String = "smallUrl",
        mediumUrl: String = "mediumUrl",
        largeUrl: String = "largeUrl",
        fullUrl: String = "fullUrl",
    ): PhotoEntity =
        mockk {
            every { this@mockk.smallUrl } returns smallUrl
            every { this@mockk.mediumUrl } returns mediumUrl
            every { this@mockk.largeUrl } returns largeUrl
            every { this@mockk.fullUrl } returns fullUrl
        }
}
