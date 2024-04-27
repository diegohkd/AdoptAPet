package com.mobdao.domain.entities

data class Pet(
    val id: String,
    val type: AnimalType,
    val name: String,
    val breeds: Breeds,
    val photos: List<Photo>
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
