package com.mobdao.adoptapet.common.testutils.domain

import com.mobdao.adoptapet.domain.models.Address
import io.mockk.every
import io.mockk.mockk

object AddressMockFactory {
    fun create(
        addressLine: String = "addressLine",
        latitude: Double = -21.312261,
        longitude: Double = -46.699523,
    ): Address =
        mockk {
            every { this@mockk.addressLine } returns addressLine
            every { this@mockk.latitude } returns latitude
            every { this@mockk.longitude } returns longitude
        }
}
