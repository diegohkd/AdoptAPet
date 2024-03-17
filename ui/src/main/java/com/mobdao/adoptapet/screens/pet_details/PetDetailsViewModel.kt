package com.mobdao.adoptapet.screens.pet_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.navigation.Destination
import com.mobdao.domain.GetCachedPetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        val petId: String? = savedStateHandle[Destination.PetDetails.PET_ID_ARG]

        if (petId != null) {
            viewModelScope.launch {
                getCachedPetUseCase.execute(petId)
                    .catch {
                        // TODO show error message
                        it.printStackTrace()
                    }
                    .collect {
                        _uiState.value = _uiState.value.copy(
                            petName = it.name,
                            photoUrl = it.photos.firstOrNull()?.largeUrl.orEmpty() // TODO improve
                        )
                    }
            }
        } else {
            // TODO show error message or pop back-stack
        }
    }
}