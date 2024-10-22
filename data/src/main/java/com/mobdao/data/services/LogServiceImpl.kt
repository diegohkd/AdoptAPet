package com.mobdao.data.services

import com.mobdao.common.config.AppConfig
import com.mobdao.data.utils.wrappers.TimberWrapper
import com.mobdao.domain.dataapi.services.LogService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LogServiceImpl
    @Inject
    constructor(
        private val appConfig: AppConfig,
        private val timber: TimberWrapper,
    ) : LogService {
        override fun init() {
            if (appConfig.isDebugBuild) {
                timber.plantDebugTree()
            }
        }
    }
