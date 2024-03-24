package com.mobdao.adoptapet.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadState.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.screens.home.HomeViewModel.NavAction.FilterClicked
import com.mobdao.adoptapet.screens.home.HomeViewModel.NavAction.PetClicked
import com.mobdao.adoptapet.screens.home.petspaging.PetsPager
import com.mobdao.common.kotlin.catchAndLogException
import com.mobdao.domain.ObserveSearchFilterUseCase
import com.mobdao.domain.UpdateCachedPetsFilterWithCurrentLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeSearchFilterUseCase: ObserveSearchFilterUseCase,
    private val updateCachedPetsFilterWithCurrentLocationUseCase: UpdateCachedPetsFilterWithCurrentLocationUseCase,
    private val petsPager: PetsPager,
) : ViewModel() {

    data class UiState(
        val locationPlaceholderIsVisible: Boolean = true,
        val observeLocationPermissionState: Boolean = true,
        val progressIndicatorIsVisible: Boolean = false,
        val nextPageProgressIndicatorIsVisible: Boolean = false,
        val emptyListPlaceholderIsVisible: Boolean = false,
        val genericErrorDialogIsVisible: Boolean = false,
        val address: String = "",
    )

    data class Pet(
        val id: String,
        val name: String,
        val breeds: Breeds,
        val thumbnailUrl: String,
    ) {
        data class Breeds(
            val primary: String?,
            val secondary: String?,
        )
    }

    sealed interface NavAction {
        data class PetClicked(val petId: String) : NavAction
        data object FilterClicked : NavAction
    }

    val items: Flow<PagingData<Pet>> = petsPager.items.cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
    val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

    private val _askLocationPermission = MutableStateFlow<Event<Unit>?>(null)
    val askLocationPermission: StateFlow<Event<Unit>?> = _askLocationPermission.asStateFlow()

    private var hasLocationPermission = false
    private val isUpdatingFilterWithCurrentLocation = MutableStateFlow(false)
    private val isRefreshingPetsList = MutableStateFlow(false)

    init {
        handleProgressIndicatorState()
        viewModelScope.launch {
            observeSearchFilterUseCase.execute()
                .catchAndLogException {
                    updateUiState(genericErrorDialogIsVisible = true)
                }
                .collect { searchFilter ->
                    if (searchFilter == null) return@collect
                    updateUiState(address = searchFilter.address.addressLine)
                    petsPager.setFilterAndRefresh(searchFilter)
                }
        }
    }

    fun onLocationPermissionStateUpdated(areAllLocationPermissionsGranted: Boolean) {
        if (areAllLocationPermissionsGranted) {
            hasLocationPermission = true
            updateUiState(
                locationPlaceholderIsVisible = false,
                observeLocationPermissionState = false,
            )

            isUpdatingFilterWithCurrentLocation.value = true
            viewModelScope.launch {
                updateCachedPetsFilterWithCurrentLocationUseCase.execute()
                    .catchAndLogException {
                        isUpdatingFilterWithCurrentLocation.value = false
                        updateUiState(genericErrorDialogIsVisible = true)
                    }
                    .collect {
                        isUpdatingFilterWithCurrentLocation.value = false
                    }
            }
        }
    }

    fun onPetsListLoadStateUpdate(
        refreshLoadState: LoadState,
        appendLoadState: LoadState,
        itemsCount: Int
    ) {
        val noPetsFound = refreshLoadState is NotLoading &&
                appendLoadState.endOfPaginationReached &&
                itemsCount == 0
        isRefreshingPetsList.value = when (refreshLoadState) {
            Loading -> true
            is Error, is NotLoading -> false
        }
        updateUiState(
            locationPlaceholderIsVisible = noPetsFound && !hasLocationPermission &&
                    !petsPager.isReady(),
            emptyListPlaceholderIsVisible = noPetsFound && hasLocationPermission
                    && petsPager.isReady(),
            genericErrorDialogIsVisible = refreshLoadState is Error || appendLoadState is Error,
            nextPageProgressIndicatorIsVisible = when (appendLoadState) {
                Loading -> true
                is Error, is NotLoading -> false
            }
        )
    }

    fun onPetClicked(id: String) {
        _navAction.value = Event(PetClicked(id))
    }

    fun onFilterClicked() {
        _navAction.value = Event(FilterClicked)
    }

    fun onRequestLocationPermissionClicked() {
        _askLocationPermission.value = Event(Unit)
    }

    fun onDismissGenericErrorDialog() {
        updateUiState(genericErrorDialogIsVisible = false)
    }

    private fun handleProgressIndicatorState() {
        viewModelScope.launch {
            combine(
                isUpdatingFilterWithCurrentLocation,
                isRefreshingPetsList
            ) { isGettingAddress, isRefreshingPetsList ->
                isGettingAddress || isRefreshingPetsList
            }
                .distinctUntilChanged()
                .collect { progressIndicatorIsVisible ->
                    updateUiState(progressIndicatorIsVisible = progressIndicatorIsVisible)
                }
        }
    }

    private fun updateUiState(
        locationPlaceholderIsVisible: Boolean? = null,
        observeLocationPermissionState: Boolean? = null,
        progressIndicatorIsVisible: Boolean? = null,
        nextPageProgressIndicatorIsVisible: Boolean? = null,
        emptyListPlaceholderIsVisible: Boolean? = null,
        genericErrorDialogIsVisible: Boolean? = null,
        address: String? = null,
    ) {
        _uiState.update {
            it.copy(
                locationPlaceholderIsVisible = locationPlaceholderIsVisible
                    ?: it.locationPlaceholderIsVisible,
                observeLocationPermissionState = observeLocationPermissionState
                    ?: it.observeLocationPermissionState,
                progressIndicatorIsVisible = progressIndicatorIsVisible
                    ?: it.progressIndicatorIsVisible,
                nextPageProgressIndicatorIsVisible = nextPageProgressIndicatorIsVisible
                    ?: it.nextPageProgressIndicatorIsVisible,
                emptyListPlaceholderIsVisible = emptyListPlaceholderIsVisible
                    ?: it.emptyListPlaceholderIsVisible,
                genericErrorDialogIsVisible = genericErrorDialogIsVisible
                    ?: it.genericErrorDialogIsVisible,
                address = address ?: it.address,
            )
        }
    }
}
