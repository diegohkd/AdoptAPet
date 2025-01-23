package com.mobdao.remote.internal.utils.factories

import com.mobdao.remote.internal.services.AuthService
import com.mobdao.remote.internal.services.GeoapifyService
import com.mobdao.remote.internal.services.PetFinderService
import com.mobdao.remote.internal.utils.factories.networkinterceptors.AccessTokenInterceptor
import javax.inject.Inject
import javax.inject.Singleton

private const val PET_FINDER_BASE_URL = "https://api.petfinder.com/v2/"
private const val GEOAPIFY_BASE_URL = "https://api.geoapify.com/v1/"

@Singleton
internal class RetrofitServicesFactory @Inject constructor(
    private val retrofitFactory: RetrofitFactory,
) {
    fun createAuthService(): AuthService =
        retrofitFactory
            .create(
                baseUrl = PET_FINDER_BASE_URL,
            ).create(AuthService::class.java)

    fun createPetFinderService(accessTokenInterceptor: AccessTokenInterceptor): PetFinderService =
        retrofitFactory
            .create(
                baseUrl = PET_FINDER_BASE_URL,
                interceptors = listOf(accessTokenInterceptor),
            ).create(PetFinderService::class.java)

    fun createGeoapifyService(accessTokenInterceptor: AccessTokenInterceptor): GeoapifyService =
        retrofitFactory
            .create(
                baseUrl = GEOAPIFY_BASE_URL,
                interceptors = listOf(accessTokenInterceptor),
            ).create(GeoapifyService::class.java)
}
