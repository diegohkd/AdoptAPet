package com.mobdao.adoptapet.common.extensions

import androidx.paging.LoadState

fun LoadState.isLoading(): Boolean =
    when (this) {
        LoadState.Loading -> true
        is LoadState.Error, is LoadState.NotLoading -> false
    }