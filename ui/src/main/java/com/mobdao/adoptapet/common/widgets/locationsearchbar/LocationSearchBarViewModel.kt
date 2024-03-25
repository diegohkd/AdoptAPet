package com.mobdao.adoptapet.common.widgets.locationsearchbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.Event
import com.mobdao.domain.usecases.location.GetAutocompleteLocationOptionsUseCase
import com.mobdao.domain.usecases.location.GetCurrentLocationUseCase
import com.mobdao.domain.models.Address
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOCATION_SEARCH_DEBOUNCE = 1000L

// TODO fix: if first click on clear and then current location, current location is not fetched

@OptIn(FlowPreview::class)
@HiltViewModel
class LocationSearchBarViewModel @Inject constructor(
    private val getAutocompleteLocationOptionsUseCase: GetAutocompleteLocationOptionsUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
) : ViewModel() {

    data class UiState(
        val selectedAddress: Address? = null,
        val locationSearchModeIsActive: Boolean = false,
        val locationProgressIndicatorIsVisible: Boolean = false,
        val locationAutocompleteAddresses: List<String> = emptyList(),
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _addressSelected = MutableStateFlow<Event<Address>?>(null)
    val addressSelected: StateFlow<Event<Address>?> = _addressSelected.asStateFlow()

    private val _errorEncountered = MutableStateFlow<Event<Throwable?>?>(null)
    val errorEncountered: StateFlow<Event<Throwable?>?> = _errorEncountered.asStateFlow()

    private val _askLocationPermission = MutableStateFlow<Event<Unit>?>(null)
    val askLocationPermission: StateFlow<Event<Unit>?> = _askLocationPermission.asStateFlow()

    // Cannot use StateFlow for the public state as it causes inconsistency issues
    var locationSearchQuery: String by mutableStateOf("")
        private set

    private var hasRequestedLocationPermission = false
    private var hasLocationPermission = false
    private var address: Address? = null
    private var locationSearchAddresses: List<Address> = emptyList()
    private val locationSearchQueryObservable = MutableSharedFlow<String>()

    init {
        viewModelScope.launch {
            locationSearchQueryObservable
                .onEach { locationSearchQuery = it }
                .debounce(LOCATION_SEARCH_DEBOUNCE)
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
                        .catch {
                            _errorEncountered.value = Event(it)
                        }
                        .collect { addresses ->
                            locationSearchAddresses = addresses
                            _uiState.update {
                                it.copy(locationAutocompleteAddresses = addresses.map { it.addressLine })
                            }
                        }
                }
        }
    }

    fun onSelectedAddressUpdated(address: String) {
        locationSearchQuery = address
    }

    fun onLocationPermissionStateUpdated(areAllLocationPermissionsGranted: Boolean) {
        hasLocationPermission = areAllLocationPermissionsGranted
        if (hasLocationPermission && hasRequestedLocationPermission) {
            updateAddressWithCurrentLocation()
        }
    }

    fun onCurrentLocationClicked() {
        if (hasLocationPermission) {
            updateAddressWithCurrentLocation()
        } else {
            hasRequestedLocationPermission = true
            _askLocationPermission.value = Event(Unit)
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

    fun onAddressItemClicked(index: Int) {
        val address = locationSearchAddresses[index]
        this.address = address
        _addressSelected.value = Event(address)
        _uiState.update { it.copy(locationSearchModeIsActive = false) }
    }

    fun onClearLocationSearchClicked() {
        viewModelScope.launch {
            locationSearchQueryObservable.emit("")
        }
    }

    private fun updateAddressWithCurrentLocation() {
        _uiState.update { it.copy(locationProgressIndicatorIsVisible = true) }
        viewModelScope.launch {
            getCurrentLocationUseCase.execute()
                .catch {
                    _uiState.update { it.copy(locationProgressIndicatorIsVisible = false) }
                    _errorEncountered.value = Event(it)
                }
                .collect {
                    _uiState.update { it.copy(locationProgressIndicatorIsVisible = false) }
                    locationSearchQuery = it.addressLine
                    _uiState.update {
                        it.copy(
                            locationSearchModeIsActive = false,
                            locationAutocompleteAddresses = emptyList()
                        )
                    }
                    locationSearchAddresses = emptyList()
                    address = it
                    _addressSelected.value = Event(it)
                }
        }
    }
}