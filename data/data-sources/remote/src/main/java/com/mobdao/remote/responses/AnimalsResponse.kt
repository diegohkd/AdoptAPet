package com.mobdao.remote.responses

data class AnimalsResponse(
    val animals: List<Animal>
)

data class Animal(
    val id: String,
    val name: String,
    val breeds: Breeds,
    val photos: List<Photo>
)

data class Breeds(
    val primary: String?,
    val secondary: String?,
)

data class Photo(
    val small: String,
    val medium: String,
    val large: String,
    val full: String,
)