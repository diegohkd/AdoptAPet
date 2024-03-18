package com.mobdao.remote.utils.factories

import com.mobdao.remote.services.AuthService
import com.mobdao.remote.services.PetFinderService
import com.mobdao.remote.utils.factories.network_interceptors.AccessTokenInterceptor
import javax.inject.Inject
import javax.inject.Singleton

private const val PET_FINDER_BASE_URL = "https://api.petfinder.com/v2/"

@Singleton
internal class RetrofitServicesFactory @Inject constructor(
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