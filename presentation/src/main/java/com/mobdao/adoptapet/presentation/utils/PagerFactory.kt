package com.mobdao.adoptapet.presentation.utils

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class PagerFactory
    @Inject
    constructor() {
        fun <Key : Any, Value : Any> create(
            config: PagingConfig,
            initialKey: Key? = null,
            remoteMediator: RemoteMediator<Key, Value>? = null,
            pagingSourceFactory: () -> PagingSource<Key, Value>,
        ): Pager<Key, Value> =
            Pager(
                config = config,
                initialKey = initialKey,
                remoteMediator = remoteMediator,
                pagingSourceFactory = pagingSourceFactory,
            )
    }
