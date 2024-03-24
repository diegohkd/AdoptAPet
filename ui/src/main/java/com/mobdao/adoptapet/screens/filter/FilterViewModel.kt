package com.mobdao.adoptapet.screens.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.screens.filter.FilterViewModel.NavAction.FilterApplied
import com.mobdao.common.kotlin.catchAndLogException
import com.mobdao.domain.GetSearchFilterUseCase
import com.mobdao.domain.SaveSearchFilterUseCase
import com.mobdao.domain.models.Address
import com.mobdao.domain.models.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val getSearchFilterUseCase: GetSearchFilterUseCase,
    private val saveSearchFilterUseCase: SaveSearchFilterUseCase,
) : ViewModel() {

    sealed interface NavAction {
        data object FilterApplied : NavAction
    }

    data class UiState(
        val selectedAddress: String = "",
        val petType: String = "",
        val genericErrorDialogIsVisible: Boolean = false,
    )

    private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
    val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var petType: String? = null
    private var address: Address? = null

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
                    _uiState.update {
                        it.copy(
                            selectedAddress = address?.addressLine.orEmpty(),
                            petType = searchFilter?.petType.orEmpty()
                        )
                    }
                }
        }
    }

    fun onFailedToGetAddress(throwable: Throwable?) {
        throwable?.let(Timber::e)
        _uiState.update { it.copy(genericErrorDialogIsVisible = true) }
    }

    fun onAddressSelected(address: Address) {
        this.address = address
        _uiState.update { it.copy(selectedAddress = address.addressLine) }
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
                    _navAction.value = Event(FilterApplied)
                }
        }
    }

    fun onDismissGenericErrorDialog() {
        _uiState.update { it.copy(genericErrorDialogIsVisible = false) }
    }
}