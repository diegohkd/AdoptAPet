package com.mobdao.domain.entities

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
    val contact: Contact?,
)

enum class AnimalType {
    DOG,
    CAT,
    RABBIT,
    SMALL_AND_FURRY,
    HORSE,
    BIRD,
    SCALES_FINS_AND_OTHER,
    BARNYARD,
}

data class Breeds(
    val primary: String?,
    val secondary: String?,
)

data class Contact(
    val email: String,
    val phone: String,
)
