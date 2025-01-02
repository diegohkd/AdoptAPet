package com.mobdao.adoptapet.presentation.screens.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.kotlin.catchAndLogException
import com.mobdao.adoptapet.domain.models.Address
import com.mobdao.adoptapet.domain.models.SearchFilter
import com.mobdao.adoptapet.domain.usecases.filter.GetSearchFilterUseCase
import com.mobdao.adoptapet.domain.usecases.filter.SaveSearchFilterUseCase
import com.mobdao.adoptapet.presentation.common.Event
import com.mobdao.adoptapet.presentation.screens.filter.FilterNavAction.FilterApplied
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.AddressSelected
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.ApplyClicked
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.FailedToGetAddress
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.PetTypeClicked
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.PetTypeSelected
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeState
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val getSearchFilterUseCase: GetSearchFilterUseCase,
    private val saveSearchFilterUseCase: SaveSearchFilterUseCase,
) : ViewModel() {
    private val _navAction = MutableStateFlow<Event<FilterNavAction>?>(null)
    val navAction: StateFlow<Event<FilterNavAction>?> = _navAction.asStateFlow()

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    private var petType: String? = null
    private val address = MutableStateFlow<Address?>(null)

    private val petTypesNames =
        listOf(
            "Dog",
            "Cat",
            "Rabbit",
            "Bird",
            "Small & Furry",
            "Horse",
            "Barnyard",
            "Scales, Fins & Other",
        )

    init {
        handleApplyButtonEnabledState()
        loadSavedFilter()
        _uiState.update {
            it.copy(
                petTypes =
                    PetTypesState(
                        types =
                            petTypesNames.map { petTypeName ->
                                PetTypeState(
                                    name = petTypeName,
                                    isSelected = false,
                                )
                            },
                        longestTypeNamePlaceholder =
                            petTypesNames.maxBy { petTypesName ->
                                petTypesName.length
                            },
                    ),
            )
        }
    }

    fun onUiAction(action: FilterUiAction) {
        when (action) {
            is AddressSelected -> onAddressSelected(address = action.address)
            is FailedToGetAddress -> onFailedToGetAddress(throwable = action.throwable)
            is PetTypeClicked -> onPetTypeClicked(type = action.petTypeState)
            is PetTypeSelected -> onPetTypeSelected(type = action.petType)
            ApplyClicked -> onApplyClicked()
            DismissGenericErrorDialog -> onDismissGenericErrorDialog()
        }
    }

    private fun onAddressSelected(address: Address?) {
        this.address.value = address
    }

    private fun onFailedToGetAddress(throwable: Throwable?) {
        throwable?.let(Timber::e)
        _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
    }

    // TODO allow single selection
    private fun onPetTypeClicked(type: PetTypeState) {
        _uiState.update {
//            val type = it.petTypes.types.find { petType -> petType.name == type.name }
//            val updatedType = type?.copy(isSelected = !type.isSelected)
            it.copy(
                petTypes =
                    it.petTypes.copy(
                        it.petTypes.types.map {
                            if (it == type) {
                                it.copy(isSelected = !it.isSelected)
                            } else {
                                it
                            }
                        },
                    ),
            )
        }
    }

    private fun onPetTypeSelected(type: String) {
        _uiState.update { it.copy(petType = type) }
        petType = type
    }

    private fun onApplyClicked() {
        val address = address.value!!

        viewModelScope.launch {
            saveSearchFilterUseCase
                .execute(
                    filter =
                        SearchFilter(
                            address = address,
                            petType = petType,
                        ),
                ).catchAndLogException {
                    _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                }.collect {
                    _navAction.value = Event(FilterApplied)
                }
        }
    }

    private fun onDismissGenericErrorDialog() {
        _uiState.update { it.copy(genericErrorDialogIsVisible = false) }
    }

    private fun handleApplyButtonEnabledState() {
        viewModelScope.launch {
            address.collect { address ->
                _uiState.update {
                    it.copy(isApplyButtonEnabled = address != null)
                }
            }
        }
    }

    private fun loadSavedFilter() {
        viewModelScope.launch {
            getSearchFilterUseCase
                .execute()
                .catchAndLogException {
                    _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                }.collect { searchFilter ->
                    address.value = searchFilter?.address
                    petType = searchFilter?.petType
                    _uiState.update {
                        it.copy(
                            initialAddress = searchFilter?.address?.addressLine.orEmpty(),
                            petType = searchFilter?.petType.orEmpty(),
                        )
                    }
                }
        }
    }
}
