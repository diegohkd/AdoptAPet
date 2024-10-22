package com.mobdao.adoptapet.screens.petdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.navigation.Destination.PetDetails.PET_ID_ARG
import com.mobdao.adoptapet.screens.petdetails.PetDetailsViewModel.NavAction.BackButtonClicked
import com.mobdao.adoptapet.screens.petdetails.PetDetailsViewModel.UiState.PetCard
import com.mobdao.adoptapet.screens.petdetails.PetDetailsViewModel.UiState.PetHeader
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
class PetDetailsViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getCachedPetUseCase: GetCachedPetUseCase,
    ) : ViewModel() {
        data class UiState(
//        val petInfo: PetInfo = PetInfo(),
            val petHeader: PetHeader = PetHeader(),
            val petCard: PetCard = PetCard(),
            val contact: Contact = Contact(),
            val genericErrorDialogIsVisible: Boolean = false,
        ) {
            data class PetInfo(
                val petHeader: Header = Header(),
                val petCard: PetCard = PetCard(),
                val contact: Contact = Contact(),
            ) {
                data class Header(
                    val photoUrl: String = "",
                    val name: String = "",
                )

                data class PetCard(
                    val breed: String = "",
                    val age: String = "",
                    val gender: String = "",
                    val size: String = "",
                    val distance: Float? = null,
                    val description: String = "",
                )

                data class Contact(
                    val email: String = "",
                    val phone: String = "",
                )
            }

            data class PetHeader(
                val photoUrl: String = "",
                val name: String = "",
            )

            data class PetCard(
                val breed: String = "",
                val age: String = "",
                val gender: String = "",
                val size: String = "",
                val distance: Float? = null,
                val description: String = "",
            )

            data class Contact(
                val email: String = "",
                val phone: String = "",
            )
        }

        sealed interface NavAction {
            data object BackButtonClicked : NavAction
        }

        private val _uiState = MutableStateFlow(UiState())
        val uiState: StateFlow<UiState> = _uiState.asStateFlow()

        private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
        val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

        init {
            val petId: String? = savedStateHandle[PET_ID_ARG]

            if (petId != null) {
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
                                        PetHeader(
                                            photoUrl =
                                                pet.photos
                                                    .firstOrNull()
                                                    ?.largeUrl
                                                    .orEmpty(),
                                            // TODO improve
                                            name = pet.name,
                                        ),
                                    petCard =
                                        PetCard(
                                            breed = pet.breeds.primary.orEmpty(),
                                            age = pet.age,
                                            gender = pet.gender,
                                            description = pet.description,
                                            size = pet.size,
                                            distance = pet.distance,
                                        ),
                                    contact =
                                        UiState.Contact(
                                            email = pet.contact.email,
                                            phone = pet.contact.phone,
                                        ),
                                )
                        }
                }
            } else {
                _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
            }
        }

        fun onBackButtonClicked() {
            _navAction.value = Event(BackButtonClicked)
        }

        fun onDismissGenericErrorDialog() {
            _uiState.update { it.copy(genericErrorDialogIsVisible = false) }
        }
    }
