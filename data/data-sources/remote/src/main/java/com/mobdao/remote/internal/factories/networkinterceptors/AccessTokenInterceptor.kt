package com.mobdao.remote.internal.factories.networkinterceptors

import com.mobdao.remote.AccessTokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

private const val HTTP_AUTHORIZATION_HEADER_NAME = "Authorization"
private const val AUTHORIZATION_METHOD = "Bearer"
private const val HTTP_UNAUTHORIZED_CODE = 401

internal class AccessTokenInterceptor @Inject constructor(
    private val accessTokenManager: AccessTokenManager,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(
            request = chain.createRequestWithAccessToken(
                accessToken = accessTokenManager.getAccessToken().orEmpty()
            )
        )

        if (response.code == HTTP_UNAUTHORIZED_CODE) {
            runBlocking {
                accessTokenManager.refreshAccessToken()
            }
            val newAccessToken = accessTokenManager.getAccessToken()
            response.close()
            return chain.proceed(
                request = chain.createRequestWithAccessToken(accessToken = newAccessToken.orEmpty())
            )
        }
        return response
    }

    private fun Interceptor.Chain.createRequestWithAccessToken(accessToken: String): Request =
        request()
            .newBuilder()
            .addHeader(
                name = HTTP_AUTHORIZATION_HEADER_NAME,
                value = "$AUTHORIZATION_METHOD $accessToken"
            )
            .build()
}
