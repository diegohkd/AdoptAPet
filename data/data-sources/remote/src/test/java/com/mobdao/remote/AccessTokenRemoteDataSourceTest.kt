package com.mobdao.remote

import com.mobdao.adoptapet.common.config.AppConfig
import com.mobdao.adoptapet.common.config.PetFinderConfig
import com.mobdao.remote.internal.responses.AccessTokenResponse
import com.mobdao.remote.internal.services.AuthService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AccessTokenRemoteDataSourceTest {
    private val petFinderConfig =
        PetFinderConfig(
            grantType = "grantType",
            clientId = "clientId",
            clientSecret = "clientSecret",
        )
    private val accessToken = "accessToken"
    private val accessTokenResponse: AccessTokenResponse =
        mockk {
            every { accessToken } returns this@AccessTokenRemoteDataSourceTest.accessToken
        }

    private val appConfig: AppConfig =
        mockk {
            every { petFinderConfig } returns this@AccessTokenRemoteDataSourceTest.petFinderConfig
        }
    private val authService: AuthService =
        mockk {
            coEvery {
                getAccessToken(
                    grantType = "grantType",
                    clientId = "clientId",
                    clientSecret = "clientSecret",
                )
            } returns accessTokenResponse
        }

    private val tested =
        AccessTokenRemoteDataSource(
            appConfig = appConfig,
            authService = authService,
        )

    @Test
    fun `when get new access token then new access token is returned`() =
        runTest {
            // given / when
            val result = tested.getNewAccessToken()

            // then
            assertEquals(result, accessToken)
        }
}
