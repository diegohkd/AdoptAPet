package com.mobdao.adoptapet.presentation.common.utils.extensions

import com.mobdao.adoptapet.domain.models.AnimalType
import com.mobdao.adoptapet.domain.models.AnimalType.BARNYARD
import com.mobdao.adoptapet.domain.models.AnimalType.BIRD
import com.mobdao.adoptapet.domain.models.AnimalType.CAT
import com.mobdao.adoptapet.domain.models.AnimalType.DOG
import com.mobdao.adoptapet.domain.models.AnimalType.HORSE
import com.mobdao.adoptapet.domain.models.AnimalType.RABBIT
import com.mobdao.adoptapet.domain.models.AnimalType.SCALES_FINS_AND_OTHER
import com.mobdao.adoptapet.domain.models.AnimalType.SMALL_AND_FURRY
import com.mobdao.adoptapet.presentation.common.theme.color.BarnyardColorSchema
import com.mobdao.adoptapet.presentation.common.theme.color.BirdColorSchema
import com.mobdao.adoptapet.presentation.common.theme.color.CatColorSchema
import com.mobdao.adoptapet.presentation.common.theme.color.ColorSchema
import com.mobdao.adoptapet.presentation.common.theme.color.DogColorSchema
import com.mobdao.adoptapet.presentation.common.theme.color.HorseColorSchema
import com.mobdao.adoptapet.presentation.common.theme.color.RabbitColorSchema
import com.mobdao.adoptapet.presentation.common.theme.color.ScalesFindsAndOtherColorSchema
import com.mobdao.adoptapet.presentation.common.theme.color.SmallAndFurryColorSchema

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
