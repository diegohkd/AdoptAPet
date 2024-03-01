package com.mobdao.adoptapet.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    companion object {
        @Provides
        @Singleton
        fun provideAppContext(app: Application): Context = app.applicationContext
    }
}