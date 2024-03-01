package com.mobdao.data.cache

data class AppConfig(
    val isDebugBuild: Boolean,
    val petFinderConfig: PetFinderConfig,
)

data class PetFinderConfig(
    val grantType: String,
    val clientId: String,
    val clientSecret: String,
)