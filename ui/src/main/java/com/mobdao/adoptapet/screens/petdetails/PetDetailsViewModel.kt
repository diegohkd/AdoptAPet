package com.mobdao.adoptapet.screens.petdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.navigation.Destination.PetDetails.PET_ID_ARG
import com.mobdao.common.kotlin.catchAndLogException
import com.mobdao.domain.usecases.pets.GetCachedPetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCachedPetUseCase: GetCachedPetUseCase,
) : ViewModel() {

    data class UiState(
        val petName: String = "",
        val photoUrl: String = "",
        val genericErrorDialogIsVisible: Boolean = false,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        val petId: String? = savedStateHandle[PET_ID_ARG]

        if (petId != null) {
            viewModelScope.launch {
                getCachedPetUseCase.execute(petId)
                    .catchAndLogException {
                        _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                    }
                    .collect { pet ->
                        if (pet == null) {
                            _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                            return@collect
                        }
                        _uiState.value = _uiState.value.copy(
                            petName = pet.name,
                            photoUrl = pet.photos.firstOrNull()?.largeUrl.orEmpty() // TODO improve
                        )
                    }
            }
        } else {
            _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
        }
    }

    fun onDismissGenericErrorDialog() {
        _uiState.update { it.copy(genericErrorDialogIsVisible = false) }
    }
}
