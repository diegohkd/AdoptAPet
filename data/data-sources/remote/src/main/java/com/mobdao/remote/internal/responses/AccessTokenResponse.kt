package com.mobdao.remote.internal.responses

import com.squareup.moshi.Json

internal data class AccessTokenResponse(
    @Json(name = "access_token")
    val accessToken: String,
)
