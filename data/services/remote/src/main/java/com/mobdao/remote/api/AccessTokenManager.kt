package com.mobdao.remote.api

interface AccessTokenManager {
    fun getAccessToken(): String?
    suspend fun refreshAccessToken()
}