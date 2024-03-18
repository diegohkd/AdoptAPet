package com.mobdao.adoptapet.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.LoadState.NotLoading
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.screens.home.HomeViewModel.NavAction.FilterClicked
import com.mobdao.adoptapet.screens.home.HomeViewModel.NavAction.PetClicked
import com.mobdao.domain.GetCurrentAddressAndSaveSearchFilterUseCase
import com.mobdao.domain.ObserveSearchFilterUseCase
import com.mobdao.domain.common_models.Breeds
import com.mobdao.domain.common_models.SearchFilter
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
        val processLocationPermission: Boolean = true,
        val progressIndicatorIsVisible: Boolean = false,
        val petsListIsVisible: Boolean = false,
        val nextPageProgressIndicatorIsVisible: Boolean = false,
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

    private var isReadyToLoadPets: Boolean = false
    private var petsPagingSource: PetsPagingSource? = null
    private var searchFilter: SearchFilter? = null
    private val isGettingAddress = MutableStateFlow(false)
    private val isRefreshingPetsList = MutableStateFlow(false)

    init {
        handleProgressIndicatorState()
        // TODO is it a good pattern? It's not clear what isGettingAddress will do
        isGettingAddress.value = true
        viewModelScope.launch {
            observeSearchFilterUseCase.execute()
                .catch { it.printStackTrace() }
                .collect { searchFilter ->
                    if (searchFilter == null) return@collect
                    this@HomeViewModel.searchFilter = searchFilter
                    petsPagingSource?.invalidate()
                }
        }
    }

    fun onLocationPermissionStateUpdated(
        areAllLocationPermissionsGranted: Boolean,
        shouldShowRationale: Boolean,
    ) {
        // TODO handle more cases
        if (!areAllLocationPermissionsGranted && !shouldShowRationale) {
            _askLocationPermission.value = Event(Unit)
        }
        if (areAllLocationPermissionsGranted) {
            // TODO improve this? This is to prevent more calls to onLocationPermissionStateUpdated
            //  when navigating back to this screen.
            _uiState.value = _uiState.value.copy(processLocationPermission = false)

            viewModelScope.launch {
                getCurrentAddressAndSaveSearchFilterUseCase.execute()
                    .catch { it.printStackTrace() }
                    .collect {
                        isGettingAddress.value = false
                        isReadyToLoadPets = true
                        _uiState.value = _uiState.value.copy(address = it.addressLine)
                    }
            }
        }
    }

    fun onPetsListLoadStateUpdate(refreshLoadState: LoadState, appendLoadState: LoadState) {
        when (refreshLoadState) {
            is LoadState.Error -> {
                /*TODO handle error */
            }
            LoadState.Loading -> {
                isRefreshingPetsList.value = true
            }
            is NotLoading -> {
                isRefreshingPetsList.value = false
            }
        }
        when (appendLoadState) {
            is LoadState.Error -> {
                /*TODO handle error */
            }
            LoadState.Loading -> {
                _uiState.value = _uiState.value.copy(nextPageProgressIndicatorIsVisible = true)
            }
            is NotLoading -> {
                _uiState.value = _uiState.value.copy(nextPageProgressIndicatorIsVisible = false)
            }
        }

        _uiState.value = _uiState.value.copy(
            petsListIsVisible = refreshLoadState is NotLoading && appendLoadState is NotLoading
        )
    }

    fun onPetClicked(id: String) {
        _navAction.value = Event(PetClicked(id))
    }

    fun onFilterClicked() {
        _navAction.value = Event(FilterClicked)
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