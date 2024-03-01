package com.mobdao.data.utils.factories

import com.mobdao.data.utils.network_interceptors.AccessTokenInterceptor
import com.mobdao.data.remote.services.AuthService
import com.mobdao.data.remote.services.PetFinderService
import javax.inject.Inject
import javax.inject.Singleton

private const val PET_FINDER_BASE_URL = "https://api.petfinder.com/v2/"

@Singleton
class RetrofitServicesFactory @Inject constructor(
    private val retrofitFactory: RetrofitFactory,
) {

    fun createAuthService(): AuthService =
        retrofitFactory.create(baseUrl = PET_FINDER_BASE_URL).create(AuthService::class.java)

    fun createPetFinderService(accessTokenInterceptor: AccessTokenInterceptor): PetFinderService =
        retrofitFactory.create(
            baseUrl = PET_FINDER_BASE_URL,
            interceptors = listOf(accessTokenInterceptor)
        ).create(PetFinderService::class.java)
}