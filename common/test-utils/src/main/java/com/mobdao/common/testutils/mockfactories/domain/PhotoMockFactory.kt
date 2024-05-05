package com.mobdao.common.testutils.mockfactories.domain

import com.mobdao.domain.models.Photo
import io.mockk.every
import io.mockk.mockk

object PhotoMockFactory {

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
