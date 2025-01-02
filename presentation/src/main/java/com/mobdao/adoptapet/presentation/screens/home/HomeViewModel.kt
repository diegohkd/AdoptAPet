package com.mobdao.adoptapet.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.NotLoading
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mobdao.adoptapet.common.kotlin.catchAndLogException
import com.mobdao.adoptapet.domain.usecases.filter.CreateAndCachePetsFilterWithCachedLocationUseCase
import com.mobdao.adoptapet.domain.usecases.filter.ObserveSearchFilterUseCase
import com.mobdao.adoptapet.presentation.common.Event
import com.mobdao.adoptapet.presentation.common.extensions.isLoading
import com.mobdao.adoptapet.presentation.screens.home.HomeNavAction.FilterClicked
import com.mobdao.adoptapet.presentation.screens.home.HomeNavAction.PetClicked
import com.mobdao.adoptapet.presentation.screens.home.HomeUiAction.DismissGenericErrorDialog
import com.mobdao.adoptapet.presentation.screens.home.HomeUiState.PetState
import com.mobdao.adoptapet.presentation.screens.home.petspaging.PetsPager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val createAndCachePetsFilterWithCachedLocationUseCase: CreateAndCachePetsFilterWithCachedLocationUseCase,
    private val observeSearchFilterUseCase: ObserveSearchFilterUseCase,
    private val petsPager: PetsPager,
) : ViewModel() {
    val items: Flow<PagingData<PetState>> = petsPager.items.cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _navAction = MutableStateFlow<Event<HomeNavAction>?>(null)
    val navAction: StateFlow<Event<HomeNavAction>?> = _navAction.asStateFlow()

    init {
        createAndCachePetsFilterWithCachedLocation()
        observeAndApplySearchFilter()
    }

    fun onPetsListLoadStateUpdate(
        refreshLoadState: LoadState,
        appendLoadState: LoadState,
        itemsCount: Int,
    ) {
        updateUiState(
            emptyListPlaceholderIsVisible =
                refreshLoadState is NotLoading &&
                    appendLoadState.endOfPaginationReached &&
                    itemsCount == 0,
            genericErrorDialogIsVisible = refreshLoadState is Error || appendLoadState is Error,
            progressIndicatorIsVisible = refreshLoadState.isLoading(),
            nextPageProgressIndicatorIsVisible = appendLoadState.isLoading(),
        )
    }

    fun onUiAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.PetClicked -> onPetClicked(pet = action.pet)
            DismissGenericErrorDialog -> onDismissGenericErrorDialog()
            HomeUiAction.FilterClicked -> onFilterClicked()
        }
    }

    private fun onPetClicked(pet: PetState) {
        _navAction.value = Event(PetClicked(pet.id, pet.type))
    }

    private fun onDismissGenericErrorDialog() {
        updateUiState(genericErrorDialogIsVisible = false)
    }

    private fun onFilterClicked() {
        _navAction.value = Event(FilterClicked)
    }

    private fun createAndCachePetsFilterWithCachedLocation() {
        viewModelScope.launch {
            createAndCachePetsFilterWithCachedLocationUseCase
                .execute()
                .catchAndLogException {
                    updateUiState(genericErrorDialogIsVisible = true)
                }.firstOrNull()
        }
    }

    private fun observeAndApplySearchFilter() {
        viewModelScope.launch {
            observeSearchFilterUseCase
                .execute()
                .catchAndLogException {
                    updateUiState(genericErrorDialogIsVisible = true)
                }.collect { searchFilter ->
                    if (searchFilter == null) return@collect
                    updateUiState(address = searchFilter.address.addressLine)
                    petsPager.setFilterAndRefresh(searchFilter)
                }
        }
    }

    private fun updateUiState(
        progressIndicatorIsVisible: Boolean? = null,
        nextPageProgressIndicatorIsVisible: Boolean? = null,
        emptyListPlaceholderIsVisible: Boolean? = null,
        genericErrorDialogIsVisible: Boolean? = null,
        address: String? = null,
    ) {
        _uiState.update {
            it.copy(
                progressIndicatorIsVisible =
                    progressIndicatorIsVisible
                        ?: it.progressIndicatorIsVisible,
                nextPageProgressIndicatorIsVisible =
                    nextPageProgressIndicatorIsVisible
                        ?: it.nextPageProgressIndicatorIsVisible,
                emptyListPlaceholderIsVisible =
                    emptyListPlaceholderIsVisible
                        ?: it.emptyListPlaceholderIsVisible,
                genericErrorDialogIsVisible =
                    genericErrorDialogIsVisible
                        ?: it.genericErrorDialogIsVisible,
                address = address ?: it.address,
            )
        }
    }
}
