package com.mobdao.adoptapet.domain.models

data class SearchFilter(
    val address: Address,
    val petType: PetType?,
    val petGenders: List<PetGender> = emptyList(),
)
