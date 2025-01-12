package com.mobdao.adoptapet.domain.entities

data class PetEntity(
    val id: String,
    val type: AnimalTypeEntity,
    val name: String,
    val breeds: BreedsEntity,
    val age: String,
    val size: String,
    val gender: String,
    val description: String,
    val distance: Float?,
    val photos: List<PhotoEntity>,
    val contact: ContactEntity?,
)

enum class AnimalTypeEntity {
    DOG,
    CAT,
    RABBIT,
    SMALL_AND_FURRY,
    HORSE,
    BIRD,
    SCALES_FINS_AND_OTHER,
    BARNYARD,
}

data class BreedsEntity(
    val primary: String?,
    val secondary: String?,
)

data class ContactEntity(
    val email: String,
    val phone: String,
)
