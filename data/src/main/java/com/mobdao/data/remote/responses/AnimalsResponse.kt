package com.mobdao.data.remote.responses

data class AnimalsResponse(
    val animals: List<Animal>
)

data class Animal(
    val id: String,
    val name: String,
    val photos: List<Photo>
)

data class Photo(
    val small: String,
    val medium: String,
    val large: String,
    val full: String,
)