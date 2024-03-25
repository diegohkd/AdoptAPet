package com.mobdao.adoptapet.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobdao.adoptapet.common.Event
import com.mobdao.adoptapet.screens.splash.SplashViewModel.NavAction.Completed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO fetch all filter options from PetFinder api

private const val MINIMUM_SPLASH_LOADING_TIME = 1500L

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    sealed interface NavAction {
        data object Completed : NavAction
    }

    private val _navAction = MutableStateFlow<Event<NavAction>?>(null)
    val navAction: StateFlow<Event<NavAction>?> = _navAction.asStateFlow()

    init {
        viewModelScope.launch {
            delay(MINIMUM_SPLASH_LOADING_TIME)
            _navAction.value = Event(Completed)
        }
    }
}