package com.mobdao.data.di

import com.mobdao.data.PetsRepositoryImpl
import com.mobdao.data.cache.AccessTokenHolder
import com.mobdao.data.cache.AppConfig
import com.mobdao.data.remote.services.AuthService
import com.mobdao.data.remote.services.PetFinderService
import com.mobdao.data.utils.factories.AppConfigFactory
import com.mobdao.data.utils.factories.RetrofitServicesFactory
import com.mobdao.data.utils.factories.SharedPreferencesFactory
import com.mobdao.data.utils.network_interceptors.AccessTokenInterceptor
import com.mobdao.domain_api.PetsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {

    @Binds
    internal abstract fun bindsPetsRepository(impl: PetsRepositoryImpl): PetsRepository
}