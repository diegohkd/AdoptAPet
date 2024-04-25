package com.mobdao.cache.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal")
internal data class Animal(
    @PrimaryKey
    val id: String,
    val name: String,
    @Embedded
    val breeds: Breeds,
    val photos: List<Photo>
)

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
