package com.mobdao.remote.internal.responses

internal data class AnimalsResponse(
    val animals: List<Animal>
)

internal data class Animal(
    val id: String,
    val name: String,
    val breeds: Breeds,
    val photos: List<Photo>
)

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
