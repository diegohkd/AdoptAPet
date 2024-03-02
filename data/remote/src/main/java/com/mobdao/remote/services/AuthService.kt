package com.mobdao.remote.services

import com.mobdao.remote.responses.AccessTokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun getAuthToken(
        @Field("grant_type")
        grantType: String,
        @Field("client_id")
        clientId: String,
        @Field("client_secret")
        clientSecret: String
    ): AccessTokenResponse
}