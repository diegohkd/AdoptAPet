package com.mobdao.adoptapet.screens.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

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
        val pets = mutableListOf<Pet>()
        for (i in 1..10) {
            pets.add(
                Pet(
                    id = i.toString(),
                    name = "Rex",
                    thumbnailUrl = "https://images.dog.ceo/breeds/eskimo/n02109961_8845.jpg"
                )
            )
        }
        _uiState.value = _uiState.value.copy(pets = pets)
    }
}