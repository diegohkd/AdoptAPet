package com.mobdao.cache.di

import android.content.Context
import com.mobdao.cache.AccessTokenHolder
import com.mobdao.cache.database.AppDatabase
import com.mobdao.cache.database.AppDatabaseFactory
import com.mobdao.cache.database.daos.AnimalDao
import com.mobdao.cache.factories.SharedPreferencesFactory
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
        fun provideDatabase(
            appContext: Context,
            appDatabaseFactory: AppDatabaseFactory
        ): AppDatabase = appDatabaseFactory.create(appContext)

        @Singleton
        @Provides
        fun provideAccessTokenHolder(
            sharedPreferencesFactory: SharedPreferencesFactory
        ): AccessTokenHolder =
            AccessTokenHolder(sharedPreferencesFactory.createForAccessTokenHolder())

        @Singleton
        @Provides
        fun provideAnimalDao(appDatabase: AppDatabase): AnimalDao = appDatabase.animalDao()
    }
}