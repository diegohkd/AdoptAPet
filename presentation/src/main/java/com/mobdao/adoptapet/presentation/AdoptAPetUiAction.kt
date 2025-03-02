package com.mobdao.adoptapet.presentation

sealed interface AdoptAPetUiAction {
    data object DismissGenericErrorDialog : AdoptAPetUiAction
}
