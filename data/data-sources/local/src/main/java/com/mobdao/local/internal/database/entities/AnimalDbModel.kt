package com.mobdao.local.internal.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal")
internal data class AnimalDbModel(
    @PrimaryKey
    val id: String,
    val type: AnimalTypeDbModel,
    val name: String,
    @Embedded
    val breeds: BreedsDbModel,
    val age: String,
    val size: String,
    val gender: String,
    val description: String,
    val distance: Float?,
    val photos: List<PhotoDbModel>,
    @Embedded
    val contact: ContactDbModel?,
)

internal enum class AnimalTypeDbModel(
    val rawValue: String,
) {
    DOG(rawValue = "Dog"),
    CAT(rawValue = "Cat"),
    RABBIT(rawValue = "Rabbit"),
    SMALL_AND_FURRY(rawValue = "Small & Furry"),
    HORSE(rawValue = "Horse"),
    BIRD(rawValue = "Bird"),
    SCALES_FINS_AND_OTHER(rawValue = "Scales, Fins & Other"),
    BARNYARD(rawValue = "Barnyard"),
}

internal data class PhotoDbModel(
    val smallUrl: String,
    val mediumUrl: String,
    val largeUrl: String,
    val fullUrl: String,
)

internal data class BreedsDbModel(
    val primaryBreed: String?,
    val secondaryBreed: String?,
)

data class ContactDbModel(
    val email: String,
    val phone: String,
)
