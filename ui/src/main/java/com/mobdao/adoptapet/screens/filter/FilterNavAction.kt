package com.mobdao.adoptapet.screens.filter

sealed interface FilterNavAction {
    data object FilterApplied : FilterNavAction
}
