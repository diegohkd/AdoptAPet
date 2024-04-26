package com.mobdao.local

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AccessTokenLocalDataSourceTest {

    private val accessToken: String = "accessToken"
    private val editor: SharedPreferences.Editor = mockk {
        every { putString(ACCESS_TOKEN_KEY, "newAccessToken") } returns this
        justRun { apply() }
    }

    private val sharedPreferences: SharedPreferences = mockk {
        every { getString(ACCESS_TOKEN_KEY, null) } returns accessToken
        every { edit() } returns editor
    }

    private val tested = AccessTokenLocalDataSource(sharedPreferences)

    @Test
    fun `given access token is null when get access token then null is returned`() {
        // given
        every { sharedPreferences.getString(ACCESS_TOKEN_KEY, null) } throws Exception()

        // when
        val result: String? = tested.getAccessToken()

        // then
        assertNull(result)
    }

    @Test
    fun `given access token is not null when get access token then access token is returned`() {
        // given / when
        val result: String? = tested.getAccessToken()

        // then
        assertEquals(result, "accessToken")
    }

    @Test
    fun `given access token when save access token then it is saved`() {
        // given / when
        tested.saveAccessToken(accessToken = "newAccessToken")

        // then
        verifyOrder {
            editor.putString(ACCESS_TOKEN_KEY, "newAccessToken")
            editor.apply()
        }
    }
}