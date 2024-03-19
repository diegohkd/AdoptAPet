package com.mobdao.remote.di

import com.mobdao.remote.services.AuthService
import com.mobdao.remote.services.GeoapifyService
import com.mobdao.remote.services.PetFinderService
import com.mobdao.remote.utils.factories.RetrofitServicesFactory
import com.mobdao.remote.utils.factories.network_interceptors.AccessTokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RemoteModule {

    companion object {

        @Singleton
        @Provides
        fun provideAuthService(
            retrofitServicesFactory: RetrofitServicesFactory
        ): AuthService = retrofitServicesFactory.createAuthService()

        @Singleton
        @Provides
        fun providePetFinderService(
            retrofitServicesFactory: RetrofitServicesFactory,
            accessTokenInterceptor: AccessTokenInterceptor,
        ): PetFinderService = retrofitServicesFactory.createPetFinderService(accessTokenInterceptor)

        @Singleton
        @Provides
        fun provideGeoapifyService(
            retrofitServicesFactory: RetrofitServicesFactory,
            accessTokenInterceptor: AccessTokenInterceptor,
        ): GeoapifyService = retrofitServicesFactory.createGeoapifyService(accessTokenInterceptor)
    }
}