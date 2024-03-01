package com.mobdao.adoptapet.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.domain.GetPetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPetsUseCase: GetPetsUseCase,
) : ViewModel() {

    data class UiState(
        val progressIndicatorIsVisible: Boolean = false,
        val pets: List<Pet> = emptyList()
    )

    data class Pet(
        val id: String,
        val name: String,
        val thumbnailUrl: String,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getPetsUseCase.execute()
                .catch { it.printStackTrace() }
                .collect { pets ->
                    _uiState.value = _uiState.value.copy(
                        pets = pets.map {
                            Pet(
                                id = it.id,
                                name = it.name,
                                thumbnailUrl = it.photos.firstOrNull()?.smallUrl.orEmpty()
                            )
                        }
                    )
                }
        }
    }
}