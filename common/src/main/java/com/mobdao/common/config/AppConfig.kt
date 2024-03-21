package com.mobdao.common.config

interface AppConfig {
    val isDebugBuild: Boolean
    val petFinderConfig: PetFinderConfig
    val geoapifyConfig: GeoapifyConfig
}

data class PetFinderConfig(
    val grantType: String,
    val clientId: String,
    val clientSecret: String,
)

data class GeoapifyConfig(
    val apiKey: String,
)
