package com.composeplayground.network.di

import com.composeplayground.network.ApiConfig
import com.composeplayground.network.adapter.ApiCallAdapterFactory
import com.composeplayground.network.api.SearchApi
import com.composeplayground.network.interceptor.ApiInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    companion object {
        private const val connectTimeoutSeconds = 5L
        private const val readTimeoutSeconds = 1 * 60L
        private const val writeTimeoutSeconds = 5 * 60L
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        apiInterceptor: ApiInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(connectTimeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(readTimeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(writeTimeoutSeconds, TimeUnit.SECONDS)
            .addInterceptor(apiInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        apiConfig: ApiConfig,
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(apiConfig.baseURL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ApiCallAdapterFactory(moshi))
            .build()

    @Singleton
    @Provides
    fun provideSearchApi(retrofit: Retrofit): SearchApi =
        retrofit.create(SearchApi::class.java)
}