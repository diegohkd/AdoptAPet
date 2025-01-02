package com.mobdao.adoptapet.common.testutils.domain.entities

import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.adoptapet.domain.entities.SearchFilterEntity
import io.mockk.every
import io.mockk.mockk

object SearchFilterEntityMockFactory {
    fun create(
        address: AddressEntity = AddressEntityMockFactory.create(),
        petType: String? = "petType",
    ): SearchFilterEntity =
        mockk {
            every { this@mockk.address } returns address
            every { this@mockk.petType } returns petType
        }
}
