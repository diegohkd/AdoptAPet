package com.mobdao.adoptapet.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.NotLoading
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.common.extensions.isLoading
import com.mobdao.adoptapet.screens.home.HomeViewModel.NavAction.*
import com.mobdao.adoptapet.screens.home.petspaging.PetsPager
import com.mobdao.common.kotlin.catchAndLogException
import com.mobdao.domain.models.AnimalType
import com.mobdao.domain.usecases.filter.CreateAndCachePetsFilterWithCachedLocationUseCase
import com.mobdao.domain.usecases.filter.ObserveSearchFilterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val createAndCachePetsFilterWithCachedLocationUseCase: CreateAndCachePetsFilterWithCachedLocationUseCase,
    private val observeSearchFilterUseCase: ObserveSearchFilterUseCase,
    private val petsPager: PetsPager,
) : ViewModel() {

    data class UiState(
        val progressIndicatorIsVisible: Boolean = false,
        val nextPageProgressIndicatorIsVisible: Boolean = false,
        val emptyListPlaceholderIsVisible: Boolean = false,
        val genericErrorDialogIsVisible: Boolean = false,
        val address: String = "",
    )

    data class Pet(
        val id: String,
        val type: AnimalType,
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
        data class PetClicked(val petId: String, val type: AnimalType) : NavAction
        data object FilterClicked : NavAction
    }

    val items: Flow<PagingData<Pet>> = petsPager.items.cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
    val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

    init {
        createAndCachePetsFilterWithCachedLocation()
        observeAndApplySearchFilter()
    }

    fun onPetsListLoadStateUpdate(
        refreshLoadState: LoadState,
        appendLoadState: LoadState,
        itemsCount: Int
    ) {
        updateUiState(
            emptyListPlaceholderIsVisible = refreshLoadState is NotLoading &&
                appendLoadState.endOfPaginationReached &&
                itemsCount == 0,
            genericErrorDialogIsVisible = refreshLoadState is Error || appendLoadState is Error,
            progressIndicatorIsVisible = refreshLoadState.isLoading(),
            nextPageProgressIndicatorIsVisible = appendLoadState.isLoading(),
        )
    }

    fun onPetClicked(pet: Pet) {
        _navAction.value = Event(PetClicked(pet.id, pet.type))
    }

    fun onFilterClicked() {
        _navAction.value = Event(FilterClicked)
    }

    fun onDismissGenericErrorDialog() {
        updateUiState(genericErrorDialogIsVisible = false)
    }

    private fun createAndCachePetsFilterWithCachedLocation() {
        viewModelScope.launch {
            createAndCachePetsFilterWithCachedLocationUseCase.execute()
                .catchAndLogException {
                    updateUiState(genericErrorDialogIsVisible = true)
                }
                .firstOrNull()
        }
    }

    private fun observeAndApplySearchFilter() {
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

    private fun updateUiState(
        progressIndicatorIsVisible: Boolean? = null,
        nextPageProgressIndicatorIsVisible: Boolean? = null,
        emptyListPlaceholderIsVisible: Boolean? = null,
        genericErrorDialogIsVisible: Boolean? = null,
        address: String? = null,
    ) {
        _uiState.update {
            it.copy(
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
