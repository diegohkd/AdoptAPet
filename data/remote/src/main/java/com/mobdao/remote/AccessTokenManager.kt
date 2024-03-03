package com.mobdao.remote

interface AccessTokenManager {
    fun getAccessToken(): String?
    suspend fun refreshAccessToken()
}