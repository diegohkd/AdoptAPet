package com.mobdao.remote.internal.utils.adapters

import com.mobdao.remote.internal.responses.AnimalType
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

internal class AnimalTypeAdapter {

    @ToJson
    fun toJson(animalType: AnimalType): String = animalType.rawValue

    @FromJson
    fun fromJson(animalType: String): AnimalType? = typeByRawValue[animalType]

    companion object {
        private val typeByRawValue = AnimalType.entries.associateBy { it.rawValue }
    }
}