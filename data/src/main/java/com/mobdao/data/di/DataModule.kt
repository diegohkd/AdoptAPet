package com.mobdao.data.di

import com.mobdao.common.config.AppConfig
import com.mobdao.data.PetsRepositoryImpl
import com.mobdao.data.utils.factories.AppConfigFactory
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

    companion object {
        @Singleton
        @Provides
        fun provideAppConfig(
            appConfigFactory: AppConfigFactory
        ): AppConfig = appConfigFactory.create()
    }
}