package com.mobdao.data.utils.factories

import com.mobdao.adoptapet.common.config.AppConfig
import com.mobdao.adoptapet.common.config.GeoapifyConfig
import com.mobdao.adoptapet.common.config.PetFinderConfig
import com.mobdao.data.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

private const val GRANT_TYPE = "client_credentials"

@Singleton
class AppConfigFactory
    @Inject
    constructor() {
        fun create(): AppConfig =
            object : AppConfig {
                override val isDebugBuild: Boolean = BuildConfig.DEBUG
                override val petFinderConfig: PetFinderConfig =
                    PetFinderConfig(
                        grantType = GRANT_TYPE,
                        clientId = BuildConfig.PET_FINDER_CLIENT_ID,
                        clientSecret = BuildConfig.PET_FINDER_CLIENT_SECRET,
                    )
                override val geoapifyConfig: GeoapifyConfig =
                    GeoapifyConfig(apiKey = BuildConfig.GEOAPIFY_API_KEY)
            }
    }
