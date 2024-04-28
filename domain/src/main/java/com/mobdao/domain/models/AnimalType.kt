package com.mobdao.domain.models

enum class AnimalType {
    DOG,
    CAT,
    RABBIT,
    SMALL_AND_FURRY,
    HORSE,
    BIRD,
    SCALES_FINS_AND_OTHER,
    BARNYARD;

    companion object {
        private val nameToType: Map<String, AnimalType> = entries.associateBy { it.name }

        fun fromName(name: String?): AnimalType? = nameToType[name]
    }
}