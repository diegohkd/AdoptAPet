package com.mobdao.adoptapet.utils

import androidx.paging.*
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class PagerFactory @Inject constructor() {

    fun <Key : Any, Value : Any> create(
        config: PagingConfig,
        initialKey: Key? = null,
        remoteMediator: RemoteMediator<Key, Value>? = null,
        pagingSourceFactory: () -> PagingSource<Key, Value>
    ): Pager<Key, Value> = Pager(
        config = config,
        initialKey = initialKey,
        remoteMediator = remoteMediator,
        pagingSourceFactory = pagingSourceFactory
    )
}
