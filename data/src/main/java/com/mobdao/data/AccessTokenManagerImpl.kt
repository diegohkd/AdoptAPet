package com.mobdao.data

import com.mobdao.cache.AccessTokenLocalDataSource
import com.mobdao.common.config.AppConfig
import com.mobdao.common.config.PetFinderConfig
import com.mobdao.remote.api.AccessTokenManager
import com.mobdao.remote.services.AuthService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AccessTokenManagerImpl @Inject constructor(
    appConfig: AppConfig,
    private val authService: AuthService,
    private val accessTokenDataSource: AccessTokenLocalDataSource,
): AccessTokenManager {

    private val petFinderConfig: PetFinderConfig = appConfig.petFinderConfig

    override fun getAccessToken(): String? = accessTokenDataSource.getAccessToken()

    override suspend fun refreshAccessToken() {
        val accessToken: String = authService.getAuthToken(
            grantType = petFinderConfig.grantType,
            clientId = petFinderConfig.clientId,
            clientSecret = petFinderConfig.clientSecret,
        ).accessToken
        accessTokenDataSource.saveAccessToken(accessToken)
    }
}