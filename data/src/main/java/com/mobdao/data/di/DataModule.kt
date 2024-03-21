package com.mobdao.data.di

import com.mobdao.common.config.AppConfig
import com.mobdao.data.repositories.GeoLocationRepositoryImpl
import com.mobdao.data.repositories.PetsRepositoryImpl
import com.mobdao.data.repositories.SearchFilterRepositoryImpl
import com.mobdao.data.services.LogServiceImpl
import com.mobdao.data.utils.AccessTokenManagerImpl
import com.mobdao.data.utils.factories.AppConfigFactory
import com.mobdao.domain.api.repositories.GeoLocationRepository
import com.mobdao.domain.api.repositories.PetsRepository
import com.mobdao.domain.api.repositories.SearchFilterRepository
import com.mobdao.domain.api.services.LogService
import com.mobdao.remote.api.AccessTokenManager
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
    internal abstract fun bindsLogService(impl: LogServiceImpl): LogService

    @Binds
    internal abstract fun bindsPetsRepository(impl: PetsRepositoryImpl): PetsRepository

    @Binds
    internal abstract fun bindsGeoLocationRepository(
        impl: GeoLocationRepositoryImpl
    ): GeoLocationRepository

    @Binds
    internal abstract fun bindsSearchFilterRepository(
        impl: SearchFilterRepositoryImpl
    ): SearchFilterRepository

    @Binds
    internal abstract fun bindsAccessTokenManager(impl: AccessTokenManagerImpl): AccessTokenManager

    companion object {
        @Singleton
        @Provides
        fun provideAppConfig(
            appConfigFactory: AppConfigFactory
        ): AppConfig = appConfigFactory.create()
    }
}
