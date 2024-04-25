package com.mobdao.cache.database.entities

import androidx.room.Entity

@Entity(
    tableName = "address",
    primaryKeys = ["latitude", "longitude"]
)
internal data class Address(
    val latitude: Double,
    val longitude: Double,
    val addressLine: String,
)