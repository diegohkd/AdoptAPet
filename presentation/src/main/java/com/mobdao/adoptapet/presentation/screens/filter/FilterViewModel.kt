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
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.BackClicked
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.FailedToGetAddress
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.PetGenderClicked
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiAction.PetTypeClicked
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.FilterProperty.PetGenderState
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.FilterProperty.PetTypeState
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetGenderNameState
import com.mobdao.adoptapet.presentation.screens.filter.FilterUiState.PetTypeNameState
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
    private val filterMapper: FilterMapper,
) : ViewModel() {
    private val _navAction = MutableStateFlow<Event<FilterNavAction>?>(null)
    val navAction: StateFlow<Event<FilterNavAction>?> = _navAction.asStateFlow()

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    private var selectedPetType: PetTypeNameState? = null
    private val address = MutableStateFlow<Address?>(null)

    init {
        handleApplyButtonEnabledState()
        loadSavedFilter()
    }

    fun onUiAction(action: FilterUiAction) {
        when (action) {
            is AddressSelected -> onAddressSelected(address = action.address)
            is FailedToGetAddress -> onFailedToGetAddress(throwable = action.throwable)
            is PetTypeClicked -> onPetTypeClicked(type = action.petTypeFilter)
            is PetGenderClicked -> onPetGenderClicked(gender = action.gender)
            ApplyClicked -> onApplyClicked()
            BackClicked -> onBackClicked()
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

    private fun onPetTypeClicked(type: PetTypeNameState) {
        selectedPetType = if (type == selectedPetType) null else type
        _uiState.update {
            it.copy(
                petTypes =
                    it.petTypes.map {
                        it.copy(isSelected = it.type == selectedPetType)
                    },
            )
        }
    }

    private fun onPetGenderClicked(gender: PetGenderNameState) {
        _uiState.update {
            it.copy(
                petGenders =
                    it.petGenders.map {
                        if (it.gender == gender) {
                            it.copy(isSelected = !it.isSelected)
                        } else {
                            it
                        }
                    },
            )
        }
    }

    private fun onApplyClicked() {
        val address = address.value!!

        viewModelScope.launch {
            saveSearchFilterUseCase
                .execute(
                    filter =
                        SearchFilter(
                            address = address,
                            petType = selectedPetType?.let(filterMapper::toDomainModel),
                            petGenders =
                                _uiState.value.petGenders
                                    .filter { it.isSelected }
                                    .map { it.gender }
                                    .map(filterMapper::toDomainModel),
                        ),
                ).catchAndLogException {
                    _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                }.collect {
                    _navAction.value = Event(FilterApplied)
                }
        }
    }

    private fun onBackClicked() {
        _navAction.value = Event(FilterNavAction.BackClicked)
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
                    selectedPetType = searchFilter?.petType?.let(filterMapper::toState)
                    val selectedPetGenders: List<PetGenderNameState> =
                        searchFilter?.petGenders?.map(filterMapper::toState).orEmpty()

                    _uiState.update {
                        it.copy(
                            initialAddress = searchFilter?.address?.addressLine.orEmpty(),
                            petTypes =
                                PetTypeNameState.entries.map { petType ->
                                    PetTypeState(
                                        type = petType,
                                        isSelected = petType == selectedPetType,
                                    )
                                },
                            petGenders =
                                PetGenderNameState.entries.map { petGender ->
                                    PetGenderState(
                                        gender = petGender,
                                        isSelected = selectedPetGenders.any { it == petGender },
                                    )
                                },
                        )
                    }
                }
        }
    }
}
