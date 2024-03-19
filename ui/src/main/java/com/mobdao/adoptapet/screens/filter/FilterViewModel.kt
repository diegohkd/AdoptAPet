package com.mobdao.adoptapet.screens.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.screens.filter.FilterViewModel.NavAction.ApplyClicked
import com.mobdao.domain.GetAutocompleteLocationOptionsUseCase
import com.mobdao.domain.GetCachedCurrentAddressUseCase
import com.mobdao.domain.SaveSearchFilterUseCase
import com.mobdao.domain.common_models.Address
import com.mobdao.domain.common_models.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val saveSearchFilterUseCase: SaveSearchFilterUseCase,
    private val getCachedCurrentAddressUseCase: GetCachedCurrentAddressUseCase,
    private val getAutocompleteLocationOptionsUseCase: GetAutocompleteLocationOptionsUseCase,
) : ViewModel() {

    sealed interface NavAction {
        data object ApplyClicked : NavAction
    }

    data class UiState(
        val locationSearchModeIsActive: Boolean = false,
        // TODO create another model for Address
        val locationAutocompleteAddresses: List<Address> = emptyList()
    )

    private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
    val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Cannot use StateFlow as it causes inconsistency issues
    var searchQuery by mutableStateOf("")
        private set

    private var petType: String = ""
    private var address: Address? = null

    init {
        // TODO disable applying filter until address is returned?
        viewModelScope.launch {
            getCachedCurrentAddressUseCase.execute()
                .catch { it.printStackTrace() }
                .collect {
                    address = it
                }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        searchQuery = newQuery
    }

    fun onLocationSearchActiveChange(isActive: Boolean) {
        _uiState.update { it.copy(locationSearchModeIsActive = isActive) }
    }

    fun onAutocompleteAddressSelected(index: Int) {
        val address = _uiState.value.locationAutocompleteAddresses[index]
        this.address = address
        _uiState.update { it.copy(locationSearchModeIsActive = false) }
        searchQuery = address.addressLine
    }

    // TODO search as location is typed with debounce included
    fun onSearch(searchQuery: String) {
        viewModelScope.launch {
            getAutocompleteLocationOptionsUseCase.execute(searchQuery)
                .catch { it.printStackTrace() }
                .collect { addresses ->
                    _uiState.update {
                        it.copy(locationAutocompleteAddresses = addresses)
                    }
                }
        }
    }

    fun onPetTypeSelected(type: String) {
        petType = type
    }

    fun onApplyClicked() {
        viewModelScope.launch {
            saveSearchFilterUseCase.execute(
                filter = SearchFilter(
                    address = address ?: return@launch, // TODO Improve
                    petType = petType
                )
            )
                .catch { it.printStackTrace() }
                .collect {
                    _navAction.value = Event(ApplyClicked)
                }
        }
    }
}