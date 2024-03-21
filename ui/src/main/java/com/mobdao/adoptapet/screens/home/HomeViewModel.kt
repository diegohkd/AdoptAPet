package com.mobdao.adoptapet.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.LoadState.*
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.screens.home.HomeViewModel.NavAction.FilterClicked
import com.mobdao.adoptapet.screens.home.HomeViewModel.NavAction.PetClicked
import com.mobdao.common.kotlin.catchAndLogException
import com.mobdao.domain.GetCurrentAddressAndSaveSearchFilterUseCase
import com.mobdao.domain.ObserveSearchFilterUseCase
import com.mobdao.domain.models.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ITEMS_PER_PAGE = 20

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeSearchFilterUseCase: ObserveSearchFilterUseCase,
    private val getCurrentAddressAndSaveSearchFilterUseCase: GetCurrentAddressAndSaveSearchFilterUseCase,
    private val petsPagingSourceFactory: PetsPagingSource.Factory,
) : ViewModel() {

    data class UiState(
        val locationPlaceholderIsVisible: Boolean = true,
        val processLocationPermission: Boolean = true,
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

    val items: Flow<PagingData<Pet>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
        pagingSourceFactory = {
            petsPagingSourceFactory.create(
                isReadyToLoad = isReadyToLoadPets,
                searchFilter = searchFilter
            ).also {
                petsPagingSource = it
            }
        },
    )
        .flow
        .cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
    val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

    private val _askLocationPermission = MutableStateFlow<Event<Unit>?>(null)
    val askLocationPermission: StateFlow<Event<Unit>?> = _askLocationPermission.asStateFlow()

    private var hasLocationPermission = false
    private var isReadyToLoadPets: Boolean = false
    private var petsPagingSource: PetsPagingSource? = null
    private var searchFilter: SearchFilter? = null
    private val isGettingAddress = MutableStateFlow(false)
    private val isRefreshingPetsList = MutableStateFlow(false)

    init {
        handleProgressIndicatorState()
        viewModelScope.launch {
            observeSearchFilterUseCase.execute()
                .catchAndLogException {
                    _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                }
                .collect { searchFilter ->
                    if (searchFilter == null) return@collect
                    isReadyToLoadPets = true
                    _uiState.update {
                        it.copy(address = searchFilter.address.addressLine)
                    }
                    this@HomeViewModel.searchFilter = searchFilter
                    petsPagingSource?.invalidate()
                }
        }
    }

    fun onLocationPermissionStateUpdated(areAllLocationPermissionsGranted: Boolean) {
        if (areAllLocationPermissionsGranted) {
            hasLocationPermission = true
            _uiState.value = _uiState.value.copy(processLocationPermission = false)
            _uiState.update {
                it.copy(
                    locationPlaceholderIsVisible = false,
                    // TODO improve this? This is to prevent more calls to onLocationPermissionStateUpdated
                    //  when navigating back to this screen.
                    processLocationPermission = false
                )
            }

            isGettingAddress.value = true
            viewModelScope.launch {
                getCurrentAddressAndSaveSearchFilterUseCase.execute()
                    .catchAndLogException {
                        isGettingAddress.value = false
                        _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
                    }
                    .collect {
                        isGettingAddress.value = false
                        _uiState.value = _uiState.value.copy(address = it.addressLine)
                    }
            }
        }
    }

    fun onPetsListLoadStateUpdate(
        refreshLoadState: LoadState,
        appendLoadState: LoadState,
        itemsCount: Int
    ) {
        when (refreshLoadState) {
            is Error -> {
                isRefreshingPetsList.value = false
                _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
            }
            Loading -> isRefreshingPetsList.value = true
            is NotLoading -> isRefreshingPetsList.value = false
        }
        when (appendLoadState) {
            is Error ->
                _uiState.update {
                    it.copy(
                        nextPageProgressIndicatorIsVisible = false,
                        genericErrorDialogIsVisible = true,
                    )
                }
            Loading -> _uiState.update { it.copy(nextPageProgressIndicatorIsVisible = true) }
            is NotLoading -> _uiState.update { it.copy(nextPageProgressIndicatorIsVisible = false) }
        }
        val noPetsFound = refreshLoadState is NotLoading &&
            appendLoadState.endOfPaginationReached &&
            itemsCount == 0
        _uiState.update {
            it.copy(
                locationPlaceholderIsVisible = noPetsFound && !hasLocationPermission,
                emptyListPlaceholderIsVisible = noPetsFound && hasLocationPermission && isReadyToLoadPets
            )
        }
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
        _uiState.update { it.copy(genericErrorDialogIsVisible = false) }
    }

    private fun handleProgressIndicatorState() {
        viewModelScope.launch {
            combine(
                isGettingAddress,
                isRefreshingPetsList
            ) { isGettingAddress, isRefreshingPetsList ->
                isGettingAddress || isRefreshingPetsList
            }
                .distinctUntilChanged()
                .collect {
                    _uiState.value = _uiState.value.copy(progressIndicatorIsVisible = it)
                }
        }
    }
}
