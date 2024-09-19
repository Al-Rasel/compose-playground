package com.composeplayground.config

import com.composeplayground.BuildConfig
import com.composeplayground.network.ApiConfig

object AppConfig : ApiConfig {
    override val baseURL: String
        get() = BuildConfig.BASE_URL
    override val token: String
        get() = BuildConfig.BASIC_AUTH_TOKEN
}