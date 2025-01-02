package com.mobdao.data.services

import com.mobdao.adoptapet.domain.dataapi.services.LogService
import com.mobdao.common.config.AppConfig
import com.mobdao.data.utils.wrappers.TimberWrapper
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
