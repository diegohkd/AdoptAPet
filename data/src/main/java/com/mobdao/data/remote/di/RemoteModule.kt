package com.mobdao.data.remote.di

import com.mobdao.data.remote.services.AuthService
import com.mobdao.data.remote.services.PetFinderService
import com.mobdao.data.utils.factories.RetrofitServicesFactory
import com.mobdao.data.utils.network_interceptors.AccessTokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RemoteModule {

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
    }
}