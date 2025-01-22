package com.mobdao.adoptapet.common.testutils.domain

import com.mobdao.adoptapet.domain.models.Address
import com.mobdao.adoptapet.domain.models.PetGender
import com.mobdao.adoptapet.domain.models.PetGender.MALE
import com.mobdao.adoptapet.domain.models.PetType
import com.mobdao.adoptapet.domain.models.PetType.DOG
import com.mobdao.adoptapet.domain.models.SearchFilter
import io.mockk.every
import io.mockk.mockk

object SearchFilterMockFactory {
    fun create(
        address: Address = AddressMockFactory.create(),
        petType: PetType? = DOG,
        petGenders: List<PetGender> = listOf(MALE),
    ): SearchFilter =
        mockk {
            every { this@mockk.address } returns address
            every { this@mockk.petType } returns petType
            every { this@mockk.petGenders } returns petGenders
        }
}
