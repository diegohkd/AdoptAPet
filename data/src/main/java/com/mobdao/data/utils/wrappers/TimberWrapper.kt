package com.mobdao.data.utils.wrappers

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimberWrapper @Inject constructor() {

    fun plantDebugTree() {
        Timber.plant(Timber.DebugTree())
    }
}
