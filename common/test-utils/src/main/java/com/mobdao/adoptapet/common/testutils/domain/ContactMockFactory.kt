package com.mobdao.adoptapet.common.testutils.domain

import com.mobdao.adoptapet.domain.models.Contact
import io.mockk.every
import io.mockk.mockk

object ContactMockFactory {
    fun create(
        email: String = "email",
        phone: String = "phone",
    ): Contact =
        mockk {
            every { this@mockk.email } returns email
            every { this@mockk.phone } returns phone
        }
}
