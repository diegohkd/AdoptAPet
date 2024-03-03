package com.mobdao.cache.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal")
data class Animal(
    @PrimaryKey
    val id: String,
    val name: String,
    val photos: List<Photo>
)

data class Photo(
    val smallUrl: String,
    val mediumUrl: String,
    val largeUrl: String,
    val fullUrl: String,
)
