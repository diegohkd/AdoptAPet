package com.mobdao.data.cache.di

import com.mobdao.data.cache.AccessTokenHolder
import com.mobdao.data.cache.AppConfig
import com.mobdao.data.utils.factories.AppConfigFactory
import com.mobdao.data.utils.factories.SharedPreferencesFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class CacheModule {

    companion object {

        @Singleton
        @Provides
        fun provideAccessTokenHolder(
            sharedPreferencesFactory: SharedPreferencesFactory
        ): AccessTokenHolder =
            AccessTokenHolder(sharedPreferencesFactory.createForAccessTokenHolder())

        @Singleton
        @Provides
        fun provideAppConfig(
            appConfigFactory: AppConfigFactory
        ): AppConfig = appConfigFactory.create()
    }
}