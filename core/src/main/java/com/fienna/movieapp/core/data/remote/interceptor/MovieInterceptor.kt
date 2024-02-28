package com.fienna.movieapp.core.data.remote.interceptor

import com.fienna.movieapp.core.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class MovieInterceptor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.BEARER_TOKEN}")
                .addHeader("accept", "application/json")
                .build()

        return chain.proceed(request)


    }
}