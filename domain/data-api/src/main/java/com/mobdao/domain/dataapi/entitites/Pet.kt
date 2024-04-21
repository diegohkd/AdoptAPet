package com.mobdao.domain.dataapi.entitites

data class Pet(
    val id: String,
    val name: String,
    val breeds: Breeds,
    val photos: List<Photo>
)

data class Breeds(
    val primary: String?,
    val secondary: String?,
)
