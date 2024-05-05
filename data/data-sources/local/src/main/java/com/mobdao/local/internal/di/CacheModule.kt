package com.mobdao.local.internal.di

import android.content.Context
import com.mobdao.local.AccessTokenLocalDataSource
import com.mobdao.local.OnboardingLocalDataSource
import com.mobdao.local.internal.common.factories.SharedPreferencesFactory
import com.mobdao.local.internal.database.AppDatabase
import com.mobdao.local.internal.database.AppDatabaseFactory
import com.mobdao.local.internal.database.daos.AddressDao
import com.mobdao.local.internal.database.daos.AnimalDao
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
