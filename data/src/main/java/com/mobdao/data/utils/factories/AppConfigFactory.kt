package com.mobdao.data.utils.factories

import com.mobdao.data.BuildConfig
import com.mobdao.data.cache.AppConfig
import com.mobdao.data.cache.PetFinderConfig
import javax.inject.Inject
import javax.inject.Singleton

private const val GRANT_TYPE = "client_credentials"

@Singleton
class AppConfigFactory @Inject constructor() {

    fun create(): AppConfig =
        AppConfig(
            isDebugBuild = BuildConfig.DEBUG,
            petFinderConfig = PetFinderConfig(
                grantType = GRANT_TYPE,
                clientId = BuildConfig.PET_FINDER_CLIENT_ID,
                clientSecret = BuildConfig.PET_FINDER_CLIENT_SECRET,
            )
        )
}