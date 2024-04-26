package com.mobdao.data.utils

import com.mobdao.local.AccessTokenLocalDataSource
import com.mobdao.remote.AccessTokenRemoteDataSource
import com.mobdao.remote.AccessTokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AccessTokenManagerImpl @Inject constructor(
    private val accessTokenRemoteDataSource: AccessTokenRemoteDataSource,
    private val accessTokenLocalDataSource: AccessTokenLocalDataSource,
) : AccessTokenManager {

    override fun getAccessToken(): String? = accessTokenLocalDataSource.getAccessToken()

    override suspend fun refreshAccessToken() {
        val accessToken: String = accessTokenRemoteDataSource.getNewAccessToken()
        accessTokenLocalDataSource.saveAccessToken(accessToken)
    }
}
