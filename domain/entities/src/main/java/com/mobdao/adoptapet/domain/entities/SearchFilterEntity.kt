package com.mobdao.adoptapet.domain.entities

data class SearchFilterEntity(
    val address: AddressEntity,
    val petType: PetTypeEntity? = null,
    val petGenders: List<PetGenderEntity> = emptyList(),
)
