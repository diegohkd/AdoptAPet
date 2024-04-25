package com.mobdao.cache.di

import android.content.Context
import com.mobdao.cache.AccessTokenLocalDataSource
import com.mobdao.cache.OnboardingLocalDataSource
import com.mobdao.cache.database.AppDatabase
import com.mobdao.cache.database.AppDatabaseFactory
import com.mobdao.cache.database.daos.AddressDao
import com.mobdao.cache.database.daos.AnimalDao
import com.mobdao.cache.common.factories.SharedPreferencesFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class CacheModule {

    companion object {

        @Singleton
        @Provides
        fun provideDatabase(
            appContext: Context,
            appDatabaseFactory: AppDatabaseFactory
        ): AppDatabase = appDatabaseFactory.create(appContext)

        @Singleton
        @Provides
        fun provideAccessTokenLocalDataSource(
            sharedPreferencesFactory: SharedPreferencesFactory
        ): AccessTokenLocalDataSource =
            AccessTokenLocalDataSource(sharedPreferencesFactory.createForAccessTokenDataSource())

        @Singleton
        @Provides
        fun provideOnboardingLocalDataSource(
            sharedPreferencesFactory: SharedPreferencesFactory
        ): OnboardingLocalDataSource =
            OnboardingLocalDataSource(sharedPreferencesFactory.createForOnboardingDataSource())

        @Singleton
        @Provides
        fun provideAddressDao(appDatabase: AppDatabase): AddressDao = appDatabase.addressDao()

        @Singleton
        @Provides
        fun provideAnimalDao(appDatabase: AppDatabase): AnimalDao = appDatabase.animalDao()
    }
}
