package com.mobdao.common.testutils.mockfactories.domain.entities

import com.mobdao.adoptapet.domain.entities.Address
import io.mockk.every
import io.mockk.mockk

object AddressEntityMockFactory {
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
