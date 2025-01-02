package com.mobdao.adoptapet.common.testutils.domain.entities

import com.mobdao.adoptapet.domain.entities.Address
import com.mobdao.adoptapet.domain.entities.SearchFilter
import io.mockk.every
import io.mockk.mockk

object SearchFilterEntityMockFactory {
    fun create(
        address: Address = AddressEntityMockFactory.create(),
        petType: String? = "petType",
    ): SearchFilter =
        mockk {
            every { this@mockk.address } returns address
            every { this@mockk.petType } returns petType
        }
}
