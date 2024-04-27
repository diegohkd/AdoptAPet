package com.mobdao.domain.models

data class Pet(
    val id: String,
    val type: AnimalType,
    val name: String,
    val breeds: Breeds,
    val photos: List<Photo>,
)
