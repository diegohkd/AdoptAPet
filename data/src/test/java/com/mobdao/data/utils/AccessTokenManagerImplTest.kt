package com.mobdao.data.utils

import com.mobdao.cache.AccessTokenLocalDataSource
import com.mobdao.remote.AccessTokenRemoteDataSource
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AccessTokenManagerImplTest {

    private val cachedAccessToken = "accessToken"
    private val newAccessToken = "newAccessToken"

    private val accessTokenRemoteDataSource: AccessTokenRemoteDataSource = mockk {
        coEvery { getNewAccessToken() } returns newAccessToken
    }
    private val accessTokenLocalDataSource: AccessTokenLocalDataSource = mockk {
        every { getAccessToken() } returns cachedAccessToken
        justRun { saveAccessToken(newAccessToken) }
    }

    private val tested = AccessTokenManagerImpl(
        accessTokenRemoteDataSource = accessTokenRemoteDataSource,
        accessTokenLocalDataSource = accessTokenLocalDataSource,
    )

    @Test
    fun `when get access token then cached access token is returned`() {
        // when
        val result = tested.getAccessToken()

        // then
        assertEquals(result, cachedAccessToken)
    }

    @Test
    fun `when refresh access token then new token is fetched and cached`() = runTest {
        // when
        tested.refreshAccessToken()

        // then
        verify { accessTokenLocalDataSource.saveAccessToken(newAccessToken) }
    }
}