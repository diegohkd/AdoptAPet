package com.mobdao.adoptapet.screens.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.screens.filter.FilterViewModel.NavAction.ApplyClicked
import com.mobdao.domain.GetCachedCurrentAddressUseCase
import com.mobdao.domain.SaveSearchFilterUseCase
import com.mobdao.domain.common_models.Address
import com.mobdao.domain.common_models.SearchFilter
import com.mobdao.domain.common_models.SearchFilter.Coordinates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val saveSearchFilterUseCase: SaveSearchFilterUseCase,
    private val getCachedCurrentAddressUseCase: GetCachedCurrentAddressUseCase,
) : ViewModel() {

    sealed interface NavAction {
        data object ApplyClicked : NavAction
    }

    private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
    val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

    private var petType: String = ""
    private var address: Address? = null

    init {
        // TODO disable applying filter until address is returned?
        viewModelScope.launch {
            getCachedCurrentAddressUseCase.execute()
                .catch { it.printStackTrace() }
                .collect {
                    address = it
                }
        }
    }

    fun onPetTypeSelected(type: String) {
        petType = type
    }

    fun onApplyClicked() {
        viewModelScope.launch {
            saveSearchFilterUseCase.execute(
                filter = SearchFilter(
                    coordinates = address?.let {
                        Coordinates(
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    },
                    petType = petType
                )
            )
                .catch { it.printStackTrace() }
                .collect {
                    _navAction.value = Event(ApplyClicked)
                }
        }
    }
}