package com.mobdao.common.testutils.mockfactories.data.local

import com.mobdao.data.common.SearchFilterCache
import io.mockk.mockk

object SearchFilterCacheMockFactory {

    fun create(): SearchFilterCache = mockk()
}