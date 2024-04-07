package com.mobdao.common.testutils.mockfactories.domain.api.entities

import com.mobdao.domain.api.entitites.Address
import com.mobdao.domain.api.entitites.SearchFilter
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
