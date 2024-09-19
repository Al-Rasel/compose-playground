package com.composeplayground.network.interceptor

import com.composeplayground.network.ApiConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.net.UnknownHostException
import javax.inject.Inject

class ApiInterceptor @Inject constructor(private val apiConfig: ApiConfig) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val token = apiConfig.token
        if (token.isNotEmpty()) {
            builder.addHeader("Authorization", "Bearer $token")
        }
        builder.addHeader("X-GitHub-Api-Version", "2022-11-28")
        builder.addHeader("Accept", "application/vnd.github+json")
        val request = builder.build()
        return try {
            chain.proceed(request)
        } catch (unknownHostException: UnknownHostException) {
            // TODO: track exception
            throw unknownHostException
        }
    }

}