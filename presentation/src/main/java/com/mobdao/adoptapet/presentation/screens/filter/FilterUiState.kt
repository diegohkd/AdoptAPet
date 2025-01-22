package com.mobdao.adoptapet.presentation.screens.filter

import androidx.annotation.StringRes
import com.mobdao.adoptapet.presentation.R
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.FilterProperty.PetGenderState
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.FilterProperty.PetTypeState

data class FilterUiState(
    val initialAddress: String = "",
    val petTypes: List<PetTypeState> = emptyList(),
    val petGenders: List<PetGenderState> = emptyList(),
    val isApplyButtonEnabled: Boolean = false,
    val genericErrorDialogIsVisible: Boolean = false,
) {
    sealed interface FilterProperty {
        val nameRes: Int
        val isSelected: Boolean

        data class PetTypeState(
            val type: PetTypeNameState,
            override val isSelected: Boolean,
        ) : FilterProperty {
            override val nameRes: Int = type.res
        }

        data class PetGenderState(
            val gender: PetGenderNameState,
            override val isSelected: Boolean,
        ) : FilterProperty {
            override val nameRes: Int = gender.res
        }
    }

    enum class PetTypeNameState(
        @StringRes val res: Int,
    ) {
        DOG(R.string.dog),
        CAT(R.string.cat),
        RABBIT(R.string.rabbit),
        BIRD(R.string.bird),
        SMALL_FURRY(R.string.small_and_furry),
        HORSE(R.string.horse),
        BARNYARD(R.string.barnyard),
        SCALES_FINS_OTHER(R.string.scales_fins_and_other),
    }

    enum class PetGenderNameState(
        @StringRes val res: Int,
    ) {
        MALE(R.string.male),
        FEMALE(R.string.female),
    }
}
