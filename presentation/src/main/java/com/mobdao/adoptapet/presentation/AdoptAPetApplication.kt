package com.mobdao.adoptapet.presentation

import android.app.Application
import com.mobdao.adoptapet.domain.usecases.InitializeAppOnAppCreatedUseCase
import com.mobdao.common.kotlin.catchAndLogException
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class AdoptAPetApplication : Application() {
    @Inject
    lateinit var initializeAppOnAppCreatedUseCase: InitializeAppOnAppCreatedUseCase

    private var applicationScope = MainScope()

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            initializeAppOnAppCreatedUseCase
                .execute()
                .catchAndLogException()
                .firstOrNull()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        applicationScope.cancel()
        applicationScope = MainScope()
    }
}
