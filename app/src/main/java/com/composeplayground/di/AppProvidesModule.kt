package com.composeplayground.di

import com.composeplayground.config.AppConfig
import com.composeplayground.network.ApiConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppProvidesModule {
    @Singleton
    @Provides
    fun provideApiConfig(): ApiConfig {
        return AppConfig
    }
}
