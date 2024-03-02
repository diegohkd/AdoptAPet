package com.mobdao.data.utils.network_interceptors

import com.mobdao.cache.AccessTokenHolder
import com.mobdao.common.config.AppConfig
import com.mobdao.common.config.PetFinderConfig
import com.mobdao.data.remote.services.AuthService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

private const val HTTP_AUTHORIZATION_HEADER_NAME = "Authorization"
private const val AUTHORIZATION_METHOD = "Bearer"
private const val HTTP_UNAUTHORIZED_CODE = 401

class AccessTokenInterceptor @Inject constructor(
    private val authService: AuthService,
    appConfig: AppConfig,
    private val accessTokenHolder: AccessTokenHolder,
) : Interceptor {

    private val petFinderConfig: PetFinderConfig = appConfig.petFinderConfig

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(
            request = chain.createRequestWithAccessToken(
                accessToken = accessTokenHolder.get().orEmpty()
            )
        )

        if (response.code == HTTP_UNAUTHORIZED_CODE) {
            val newAccessToken = runBlocking {
                authService.getAuthToken(
                    grantType = petFinderConfig.grantType,
                    clientId = petFinderConfig.clientId,
                    clientSecret = petFinderConfig.clientSecret,
                ).accessToken
            }
            accessTokenHolder.save(newAccessToken)
            response.close()
            return chain.proceed(
                request = chain.createRequestWithAccessToken(accessToken = newAccessToken)
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