package com.mobdao.adoptapet.presentation.screens.filter

sealed interface FilterNavAction {
    data object FilterApplied : FilterNavAction

    data object BackClicked : FilterNavAction
}
