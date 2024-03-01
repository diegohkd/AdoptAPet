package com.mobdao.data.utils.factories

import com.mobdao.data.cache.AppConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

// TODO maybe create different factory depending on the buildType
@Singleton
class RetrofitFactory @Inject constructor(
    private val appConfig: AppConfig,
) {

    fun create(baseUrl: String, interceptors: List<Interceptor> = emptyList()): Retrofit {
        val client = OkHttpClient.Builder()
        if (appConfig.isDebugBuild) {
            val interceptor = HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
            client.addInterceptor(interceptor)
        }
        interceptors.forEach(client::addInterceptor)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client.build())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .build()
    }
}