package com.mobdao.local.internal.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal")
internal data class Animal(
    @PrimaryKey
    val id: String,
    val type: AnimalType,
    val name: String,
    @Embedded
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

internal data class Photo(
    val smallUrl: String,
    val mediumUrl: String,
    val largeUrl: String,
    val fullUrl: String,
)

internal data class Breeds(
    val primaryBreed: String?,
    val secondaryBreed: String?,
)
