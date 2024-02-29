package com.fienna.movieapp.core.data.remote.datasource

import com.fienna.movieapp.core.data.remote.data.CreditResponse
import com.fienna.movieapp.core.data.remote.data.DetailMovieResponse
import com.fienna.movieapp.core.data.remote.data.NowPlayingResponse
import com.fienna.movieapp.core.data.remote.data.PopularResponse
import com.fienna.movieapp.core.data.remote.data.UpComingResponse
import com.fienna.movieapp.core.data.remote.service.ApiEndPoint
import com.fienna.movieapp.core.utils.safeApiCall

class RemoteDataSource(private val apiEndPoint: ApiEndPoint) {
    suspend fun fetchNowPlayingMovie(): NowPlayingResponse = safeApiCall {
        apiEndPoint.fetchNowPlayingMovie()
    }

    suspend fun fetchPopularMovie(): PopularResponse = safeApiCall {
        apiEndPoint.fetchPopularMovie()
    }

    suspend fun fetchUpcomingMovie(): UpComingResponse = safeApiCall {
        apiEndPoint.fetchUpcomingMovie()
    }

    suspend fun fetchDetailMovie(movieId:Int?=null):DetailMovieResponse = safeApiCall {
        apiEndPoint.fetchDetailMovie(movieId)
    }

    suspend fun fetchCreditMovie(movieId:Int?=null):CreditResponse = safeApiCall {
        apiEndPoint.fetchCreditMovie(movieId)
    }


}