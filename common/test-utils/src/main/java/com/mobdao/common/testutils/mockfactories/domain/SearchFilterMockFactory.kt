package com.mobdao.common.testutils.mockfactories.domain

import com.mobdao.domain.models.Address
import com.mobdao.domain.models.SearchFilter
import io.mockk.every
import io.mockk.mockk

object SearchFilterMockFactory {
    fun create(
        address: Address = AddressMockFactory.create(),
        petType: String? = "petType",
    ): SearchFilter =
        mockk {
            every { this@mockk.address } returns address
            every { this@mockk.petType } returns petType
        }
}
