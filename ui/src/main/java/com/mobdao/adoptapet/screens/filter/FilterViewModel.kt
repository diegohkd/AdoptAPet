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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
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
        val locationProgressIndicatorIsVisible: Boolean = false,
        val locationAutocompleteAddresses: List<String> = emptyList()
    )

    private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
    val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    // Cannot use StateFlow for the public state as it causes inconsistency issues
    private val locationSearchQueryObservable = MutableSharedFlow<String>()
    var locationSearchQuery: String by mutableStateOf("")
        private set

    private var petType: String = ""
    private var address: Address? = null
    private var locationSearchAddresses: List<Address> = emptyList()

    init {
        // TODO add current location option
        // TODO move everything in the init to functions?
        // TODO update UI with current filter applied
        // TODO add state to the property names that are not in the UiState?

        // TODO disable applying filter until address is returned?
        viewModelScope.launch {
            getCachedCurrentAddressUseCase.execute()
                .catch { it.printStackTrace() }
                .collect {
                    address = it
                }
        }
        viewModelScope.launch {
            locationSearchQueryObservable
                .distinctUntilChanged()
                .onEach { locationSearchQuery = it }
                .debounce(1000)
                .collect { searchQuery ->
                    getAutocompleteLocationOptionsUseCase.execute(searchQuery)
                        .onStart {
                            _uiState.update {
                                it.copy(locationProgressIndicatorIsVisible = true)
                            }
                        }
                        .onCompletion {
                            _uiState.update {
                                it.copy(locationProgressIndicatorIsVisible = false)
                            }
                        }
                        .catch { it.printStackTrace() } // TODO show error state
                        .collect { addresses ->
                            locationSearchAddresses = addresses
                            _uiState.update {
                                it.copy(locationAutocompleteAddresses = addresses.map { it.addressLine })
                            }
                        }
                }
        }
    }

    fun onLocationSearchQueryChanged(newQuery: String) {
        viewModelScope.launch {
            locationSearchQueryObservable.emit(newQuery)
        }
    }

    fun onLocationSearchActiveChange(isActive: Boolean) {
        _uiState.update { it.copy(locationSearchModeIsActive = isActive) }
    }

    fun onAddressSelected(index: Int) {
        val address = locationSearchAddresses[index]
        this.address = address
        _uiState.update { it.copy(locationSearchModeIsActive = false) }
        locationSearchQuery = address.addressLine
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
                .catch { it.printStackTrace() } // TODO improve
                .collect {
                    _navAction.value = Event(ApplyClicked)
                }
        }
    }
}