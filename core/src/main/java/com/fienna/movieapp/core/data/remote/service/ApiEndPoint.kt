package com.fienna.movieapp.core.data.remote.service

import com.fienna.movieapp.core.data.remote.data.CreditResponse
import com.fienna.movieapp.core.data.remote.data.DetailMovieResponse
import com.fienna.movieapp.core.data.remote.data.NowPlayingResponse
import com.fienna.movieapp.core.data.remote.data.PopularResponse
import com.fienna.movieapp.core.data.remote.data.SearchResponse
import com.fienna.movieapp.core.data.remote.data.UpComingResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndPoint {
    @GET("movie/now_playing?page=3")
    suspend fun fetchNowPlayingMovie(): NowPlayingResponse

    @GET("movie/popular?page=2")
    suspend fun fetchPopularMovie(): PopularResponse

    @GET("movie/upcoming")
    suspend fun fetchUpcomingMovie(): UpComingResponse

    @GET("movie/{id}")
    suspend fun fetchDetailMovie(@Path("id") movieId:Int?=null):DetailMovieResponse

    @GET("movie/{id}/credits")
    suspend fun fetchCreditMovie(@Path("id") movieId:Int?=null):CreditResponse

    @GET("search/movie")
    suspend fun fetchSearchMovie(
        @Query("query") query :String? = "",
        @Query("page") page:Int? = null
    ):SearchResponse

}