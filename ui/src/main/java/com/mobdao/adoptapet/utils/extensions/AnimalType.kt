package com.mobdao.adoptapet.utils.extensions

import com.mobdao.adoptapet.common.theme.color.*
import com.mobdao.domain.models.AnimalType
import com.mobdao.domain.models.AnimalType.*

fun AnimalType.toColorSchema(): ColorSchema =
    when (this) {
        DOG -> DogColorSchema
        CAT -> CatColorSchema
        RABBIT -> RabbitColorSchema
        SMALL_AND_FURRY -> SmallAndFurryColorSchema
        HORSE -> HorseColorSchema
        BIRD -> BirdColorSchema
        SCALES_FINS_AND_OTHER -> ScalesFindsAndOtherColorSchema
        BARNYARD -> BarnyardColorSchema
    }
