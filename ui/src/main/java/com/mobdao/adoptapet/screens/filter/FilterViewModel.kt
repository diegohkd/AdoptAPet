package com.mobdao.adoptapet.screens.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.screens.filter.FilterViewModel.NavAction.ApplyClicked
import com.mobdao.common.kotlin.catchAndLogException
import com.mobdao.domain.GetAutocompleteLocationOptionsUseCase
import com.mobdao.domain.GetCachedCurrentAddressUseCase
import com.mobdao.domain.GetSearchFilterUseCase
import com.mobdao.domain.SaveSearchFilterUseCase
import com.mobdao.domain.models.Address
import com.mobdao.domain.models.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOCATION_SEARCH_DEBOUNCE = 1000L

@OptIn(FlowPreview::class)
@HiltViewModel
class FilterViewModel @Inject constructor(
    private val getSearchFilterUseCase: GetSearchFilterUseCase,
    private val saveSearchFilterUseCase: SaveSearchFilterUseCase,
    private val getCachedCurrentAddressUseCase: GetCachedCurrentAddressUseCase,
    private val getAutocompleteLocationOptionsUseCase: GetAutocompleteLocationOptionsUseCase,
) : ViewModel() {

    sealed interface NavAction {
        data object ApplyClicked : NavAction
    }

    data class UiState(
        val locationSearchModeIsActive: Boolean = false,
        val isSelectingCurrentLocationEnabled: Boolean = false,
        val locationProgressIndicatorIsVisible: Boolean = false,
        val locationAutocompleteAddresses: List<String> = emptyList(),
        val petType: String = "",
        val genericErrorDialogIsVisible: Boolean = false,
    )

    private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
    val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Cannot use StateFlow for the public state as it causes inconsistency issues
    private val locationSearchQueryObservable = MutableSharedFlow<String>()
    var locationSearchQuery: String by mutableStateOf("")
        private set

    private var petType: String? = null
    private var address: Address? = null
    private var locationSearchAddresses: List<Address> = emptyList()

    init {
        // TODO move everything in the init to functions?
        // TODO add state to the property names that are not in the UiState?
        // TODO disable applying filter until address is returned?

        viewModelScope.launch {
            getSearchFilterUseCase.execute()
                .catchAndLogException {
                    _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                }
                .collect { searchFilter ->
                    address = searchFilter?.address
                    petType = searchFilter?.petType
                    locationSearchQuery = address?.addressLine.orEmpty()
                    _uiState.update { it.copy(petType = searchFilter?.petType.orEmpty()) }
                }
        }
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
                        .catchAndLogException {
                            _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
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

    fun onLocationPermissionStateUpdated(areAllLocationPermissionsGranted: Boolean) {
        _uiState.update {
            it.copy(isSelectingCurrentLocationEnabled = areAllLocationPermissionsGranted)
        }
    }

    fun onCurrentLocationClicked() {
        viewModelScope.launch {
            getCachedCurrentAddressUseCase.execute()
                .catchAndLogException {
                    _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                }
                .collect {
                    if (it == null) {
                        _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                        return@collect
                    }
                    locationSearchQuery = it.addressLine
                    _uiState.update {
                        it.copy(
                            locationSearchModeIsActive = false,
                            locationAutocompleteAddresses = emptyList()
                        )
                    }
                    locationSearchAddresses = emptyList()
                    address = it
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

    fun onClearLocationSearchClicked() {
        viewModelScope.launch {
            locationSearchQueryObservable.emit("")
        }
    }

    fun onPetTypeSelected(type: String) {
        _uiState.update { it.copy(petType = type) }
        petType = type
    }

    fun onApplyClicked() {
        val address = address ?: run {
            _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
            return
        }

        viewModelScope.launch {
            saveSearchFilterUseCase.execute(
                filter = SearchFilter(
                    address = address,
                    petType = petType
                )
            )
                .catchAndLogException {
                    _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                }
                .collect {
                    _navAction.value = Event(ApplyClicked)
                }
        }
    }

    fun onDismissGenericErrorDialog() {
        _uiState.update { it.copy(genericErrorDialogIsVisible = false) }
    }
}
