package com.mobdao.adoptapet.presentation.screens.filter

import com.mobdao.adoptapet.domain.models.PetGender
import com.mobdao.adoptapet.domain.models.PetType
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetGenderNameState
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetGenderNameState.FEMALE
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetGenderNameState.MALE
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeNameState
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeNameState.BARNYARD
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeNameState.BIRD
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeNameState.CAT
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeNameState.DOG
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeNameState.HORSE
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeNameState.RABBIT
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeNameState.SCALES_FINS_OTHER
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeNameState.SMALL_FURRY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterMapper @Inject constructor() {
    fun toDomainModel(model: PetTypeNameState): PetType =
        when (model) {
            DOG -> PetType.DOG
            CAT -> PetType.CAT
            RABBIT -> PetType.RABBIT
            BIRD -> PetType.BIRD
            SMALL_FURRY -> PetType.SMALL_FURRY
            HORSE -> PetType.HORSE
            BARNYARD -> PetType.BARNYARD
            SCALES_FINS_OTHER -> PetType.SCALES_FINS_OTHER
        }

    fun toDomainModel(gender: PetGenderNameState): PetGender =
        when (gender) {
            MALE -> PetGender.MALE
            FEMALE -> PetGender.FEMALE
        }

    fun toState(model: PetType): PetTypeNameState =
        when (model) {
            PetType.DOG -> DOG
            PetType.CAT -> CAT
            PetType.RABBIT -> RABBIT
            PetType.BIRD -> BIRD
            PetType.SMALL_FURRY -> SMALL_FURRY
            PetType.HORSE -> HORSE
            PetType.BARNYARD -> BARNYARD
            PetType.SCALES_FINS_OTHER -> SCALES_FINS_OTHER
        }

    fun toState(model: PetGender): PetGenderNameState =
        when (model) {
            PetGender.MALE -> MALE
            PetGender.FEMALE -> FEMALE
        }
}
