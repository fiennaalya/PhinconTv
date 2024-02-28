package com.fienna.movieapp.core.data.remote.client

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.fienna.movieapp.core.BuildConfig
import com.fienna.movieapp.core.data.remote.interceptor.MovieInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieClient(
    val movieInterceptor: MovieInterceptor,
    val chuckerInterceptor: ChuckerInterceptor
) {
    inline fun <reified I> create():I{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(movieInterceptor)
            .addInterceptor(chuckerInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(I::class.java)
    }
}