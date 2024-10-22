package com.mobdao.domain.models

data class Pet(
    val id: String,
    val type: AnimalType,
    val name: String,
    val breeds: Breeds,
    val age: String,
    val size: String,
    val gender: String,
    val description: String,
    val distance: Float?,
    val photos: List<Photo>,
    val contact: Contact,
)
