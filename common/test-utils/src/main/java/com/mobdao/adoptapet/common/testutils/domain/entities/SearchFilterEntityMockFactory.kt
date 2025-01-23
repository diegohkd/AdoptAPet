package com.mobdao.adoptapet.common.testutils.domain.entities

import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.adoptapet.domain.entities.PetGenderEntity
import com.mobdao.adoptapet.domain.entities.PetGenderEntity.MALE
import com.mobdao.adoptapet.domain.entities.PetTypeEntity
import com.mobdao.adoptapet.domain.entities.PetTypeEntity.DOG
import com.mobdao.adoptapet.domain.entities.SearchFilterEntity
import io.mockk.every
import io.mockk.mockk

object SearchFilterEntityMockFactory {
    fun create(
        address: AddressEntity = AddressEntityMockFactory.create(),
        petType: PetTypeEntity? = DOG,
        petGenders: List<PetGenderEntity> = listOf(MALE),
    ): SearchFilterEntity =
        mockk {
            every { this@mockk.address } returns address
            every { this@mockk.petType } returns petType
            every { this@mockk.petGenders } returns petGenders
        }
}
