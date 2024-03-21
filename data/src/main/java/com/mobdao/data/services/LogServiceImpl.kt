package com.mobdao.data.services

import com.mobdao.common.config.AppConfig
import com.mobdao.domain.api.services.LogService
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LogServiceImpl @Inject constructor(private val appConfig: AppConfig) : LogService {

    override fun init() {
        if (appConfig.isDebugBuild) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
