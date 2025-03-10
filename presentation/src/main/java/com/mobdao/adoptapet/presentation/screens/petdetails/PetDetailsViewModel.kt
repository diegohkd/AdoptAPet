package com.mobdao.adoptapet.presentation.screens.petdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mobdao.adoptapet.common.kotlin.catchAndLogException
import com.mobdao.adoptapet.domain.usecases.pets.GetCachedPetUseCase
import com.mobdao.adoptapet.presentation.common.Event
import com.mobdao.adoptapet.presentation.common.utils.PetUtils
import com.mobdao.adoptapet.presentation.navigation.Destination
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsNavAction.BackButtonClicked
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiState.ContactState
import com.mobdao.adoptapet.presentation.screens.petdetails.PetDetailsUiState.PetHeaderState
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
    private val petUtils: PetUtils,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PetDetailsUiState())
    val uiState: StateFlow<PetDetailsUiState> = _uiState.asStateFlow()

    private val _navAction = MutableStateFlow<Event<PetDetailsNavAction>?>(null)
    val navAction: StateFlow<Event<PetDetailsNavAction>?> = _navAction.asStateFlow()

    init {
        val petId: String = savedStateHandle.toRoute<Destination.PetDetails>().petId

        viewModelScope.launch {
            getCachedPetUseCase
                .execute(petId)
                .catchAndLogException {
                    _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                }.collect { pet ->
                    if (pet == null) {
                        _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                        return@collect
                    }
                    _uiState.value =
                        _uiState.value.copy(
                            petHeader =
                                PetHeaderState(
                                    photoUrl =
                                        pet.photos
                                            .firstOrNull()
                                            ?.largeUrl
                                            .orEmpty(),
                                    // TODO improve
                                    name = pet.name,
                                ),
                            petCard =
                                PetDetailsUiState.PetDetailsCardState(
                                    breed = pet.breeds.primary.orEmpty(),
                                    age = pet.age,
                                    gender = pet.gender,
                                    description = petUtils.formattedDescriptionWorkaround(pet.description),
                                    size = pet.size,
                                    distance = pet.distance,
                                ),
                            contact =
                                ContactState(
                                    email = pet.contact.email,
                                    phone = pet.contact.phone,
                                ),
                        )
                }
        }
    }

    fun onUiAction(action: PetDetailsUiAction) {
        when (action) {
            PetDetailsUiAction.BackButtonClicked -> onBackButtonClicked()
            DismissGenericErrorDialog -> onDismissGenericErrorDialog()
        }
    }

    private fun onBackButtonClicked() {
        _navAction.value = Event(BackButtonClicked)
    }

    private fun onDismissGenericErrorDialog() {
        _uiState.update { it.copy(genericErrorDialogIsVisible = false) }
    }
}
