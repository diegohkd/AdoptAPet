package com.mobdao.remote.internal.responses

internal data class AnimalsResponse(
    val animals: List<Animal>
)

internal data class Animal(
    val id: String,
    val type: AnimalType,
    val name: String,
    val breeds: Breeds,
    val photos: List<Photo>
)

internal enum class AnimalType(val rawValue: String) {
    DOG(rawValue = "Dog"),
    CAT(rawValue = "Cat"),
    RABBIT(rawValue = "Rabbit"),
    SMALL_AND_FURRY(rawValue = "Small & Furry"),
    HORSE(rawValue = "Horse"),
    BIRD(rawValue = "Bird"),
    SCALES_FINS_AND_OTHER(rawValue = "Scales, Fins & Other"),
    BARNYARD(rawValue = "Barnyard"),
}

internal data class Breeds(
    val primary: String?,
    val secondary: String?,
)

internal data class Photo(
    val small: String,
    val medium: String,
    val large: String,
    val full: String,
)
