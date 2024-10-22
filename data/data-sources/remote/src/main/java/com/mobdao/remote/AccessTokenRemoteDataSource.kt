package com.mobdao.remote

import com.mobdao.common.config.AppConfig
import com.mobdao.common.config.PetFinderConfig
import com.mobdao.remote.internal.services.AuthService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessTokenRemoteDataSource
    @Inject
    internal constructor(
        appConfig: AppConfig,
        private val authService: AuthService,
    ) {
        private val petFinderConfig: PetFinderConfig = appConfig.petFinderConfig

        suspend fun getNewAccessToken(): String =
            authService
                .getAccessToken(
                    grantType = petFinderConfig.grantType,
                    clientId = petFinderConfig.clientId,
                    clientSecret = petFinderConfig.clientSecret,
                ).accessToken
    }
