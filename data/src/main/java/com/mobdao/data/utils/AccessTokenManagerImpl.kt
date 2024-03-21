package com.mobdao.data.utils

import com.mobdao.cache.AccessTokenLocalDataSource
import com.mobdao.remote.AccessTokenRemoteDataSource
import com.mobdao.remote.api.AccessTokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AccessTokenManagerImpl @Inject constructor(
    private val accessTokenRemoteDataSource: AccessTokenRemoteDataSource,
    private val accessTokenDataSource: AccessTokenLocalDataSource,
) : AccessTokenManager {

    override fun getAccessToken(): String? = accessTokenDataSource.getAccessToken()

    override suspend fun refreshAccessToken() {
        val accessToken: String = accessTokenRemoteDataSource.getNewAccessToken()
        accessTokenDataSource.saveAccessToken(accessToken)
    }
}
