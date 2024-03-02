package com.mobdao.common.di

import com.mobdao.common.utils.factories.MoshiFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class CommonModule {

    companion object {

        @Singleton
        @Provides
        fun provideMoshi(moshiFactory: MoshiFactory): Moshi = moshiFactory.create()
    }
}