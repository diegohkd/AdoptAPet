package com.mobdao.adoptapet.common.widgets.locationsearchbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.Event
import com.mobdao.domain.models.Address
import com.mobdao.domain.usecases.location.GetAutocompleteLocationOptionsUseCase
import com.mobdao.domain.usecases.location.GetCurrentLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOCATION_SEARCH_DEBOUNCE = 1000L

@OptIn(FlowPreview::class)
@HiltViewModel
class LocationSearchBarViewModel
    @Inject
    constructor(
        private val getAutocompleteLocationOptionsUseCase: GetAutocompleteLocationOptionsUseCase,
        private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    ) : ViewModel() {
        data class UiState(
            val selectedAddress: Address? = null,
            val searchModeIsActive: Boolean = false,
            val progressIndicatorIsVisible: Boolean = false,
            val locationAutocompleteAddresses: List<String> = emptyList(),
        )

        data class SelectedAddressHolder(
            val address: Address?,
        )

        private val _uiState = MutableStateFlow(UiState())
        val uiState: StateFlow<UiState> = _uiState.asStateFlow()

        private val _addressSelected = MutableStateFlow<Event<SelectedAddressHolder?>?>(null)
        val addressSelected: StateFlow<Event<SelectedAddressHolder?>?> = _addressSelected.asStateFlow()

        private val _errorEncountered = MutableStateFlow<Event<Throwable?>?>(null)
        val errorEncountered: StateFlow<Event<Throwable?>?> = _errorEncountered.asStateFlow()

        private val _requestLocationPermission = MutableStateFlow<Event<Unit>?>(null)
        val requestLocationPermission = _requestLocationPermission.asStateFlow()

        // Cannot use StateFlow for the public state as it causes inconsistency issues
        var locationSearchQuery: String by mutableStateOf("")
            private set

        private var hasRequestedLocationPermission = false
        private var hasLocationPermission = false
        private var address: Address? = null
        private var locationSearchAddresses: List<Address> = emptyList()
        private val locationSearchQueryObservable = MutableSharedFlow<String>()
        private var autocompleteLocationJob: Job? = null
        private var currentLocationJob: Job? = null

        init {
            getAutocompleteOptionsOnSearchQueryChanged()
        }

        fun init(address: String) {
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
                _requestLocationPermission.value = Event(Unit)
            }
        }

        fun onLocationSearchQueryChanged(newQuery: String) {
            locationSearchQuery = newQuery
            viewModelScope.launch {
                locationSearchQueryObservable.emit(newQuery)
            }
        }

        fun onSearchModeActiveChange(isActive: Boolean) {
            _uiState.update { it.copy(searchModeIsActive = isActive) }
        }

        fun onAddressItemClicked(index: Int) {
            autocompleteLocationJob?.cancelChildren()
            val address = locationSearchAddresses[index]
            clearCurrentSearchResults()
            _uiState.update { it.copy(searchModeIsActive = false) }

            this.address = address
            locationSearchQuery = address.addressLine
            _addressSelected.value = Event(SelectedAddressHolder(address))
        }

        fun onClearLocationSearchClicked() {
            autocompleteLocationJob?.cancel()
            locationSearchQuery = ""
            address = null
            _addressSelected.value = Event(SelectedAddressHolder(address = null))
            clearCurrentSearchResults()
        }

        private fun getAutocompleteOptionsOnSearchQueryChanged() {
            viewModelScope.launch {
                locationSearchQueryObservable
                    .debounce(LOCATION_SEARCH_DEBOUNCE)
                    .collect { searchQuery ->
                        getAutocompleteOptions(searchQuery)
                    }
            }
        }

        private fun getAutocompleteOptions(searchQuery: String) {
            currentLocationJob?.cancel()
            autocompleteLocationJob =
                viewModelScope.launch {
                    getAutocompleteLocationOptionsUseCase
                        .execute(searchQuery)
                        .onStart {
                            _uiState.update {
                                it.copy(progressIndicatorIsVisible = true)
                            }
                        }.onCompletion {
                            _uiState.update {
                                it.copy(progressIndicatorIsVisible = false)
                            }
                        }.catch {
                            _errorEncountered.value = Event(it)
                        }.collect { addresses ->
                            locationSearchAddresses = addresses
                            _uiState.update {
                                it.copy(locationAutocompleteAddresses = addresses.map { it.addressLine })
                            }
                        }
                }
        }

        private fun updateAddressWithCurrentLocation() {
            _uiState.update { it.copy(progressIndicatorIsVisible = true) }
            currentLocationJob =
                viewModelScope.launch {
                    getCurrentLocationUseCase
                        .execute()
                        .catch {
                            _uiState.update { it.copy(progressIndicatorIsVisible = false) }
                            _errorEncountered.value = Event(it)
                        }.collect { address ->
                            _uiState.update {
                                it.copy(
                                    progressIndicatorIsVisible = false,
                                    searchModeIsActive = false,
                                )
                            }
                            clearCurrentSearchResults()
                            locationSearchQuery = address.addressLine
                            this@LocationSearchBarViewModel.address = address
                            _addressSelected.value = Event(SelectedAddressHolder(address))
                        }
                }
        }

        private fun clearCurrentSearchResults() {
            locationSearchAddresses = emptyList()
            _uiState.update { it.copy(locationAutocompleteAddresses = emptyList()) }
        }
    }
