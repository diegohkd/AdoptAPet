package com.mobdao.adoptapet.common.testutils.domain.entities

import com.mobdao.adoptapet.domain.entities.AddressEntity
import io.mockk.every
import io.mockk.mockk

object AddressEntityMockFactory {
    fun create(
        addressLine: String = "addressLine",
        latitude: Double = -21.312261,
        longitude: Double = -46.699523,
    ): AddressEntity =
        mockk {
            every { this@mockk.addressLine } returns addressLine
            every { this@mockk.latitude } returns latitude
            every { this@mockk.longitude } returns longitude
        }
}
