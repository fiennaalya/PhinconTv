package com.fienna.movieapp.core.domain.repository

import androidx.paging.PagingData
import com.fienna.movieapp.core.data.remote.data.CreditResponse
import com.fienna.movieapp.core.data.remote.data.DetailMovieResponse
import com.fienna.movieapp.core.data.remote.data.NowPlayingResponse
import com.fienna.movieapp.core.data.remote.data.PopularResponse
import com.fienna.movieapp.core.data.remote.data.UpComingResponse
import com.fienna.movieapp.core.data.remote.datasource.PagingDataSourceImpl
import com.fienna.movieapp.core.data.remote.datasource.RemoteDataSource
import com.fienna.movieapp.core.domain.model.DataSearch
import com.fienna.movieapp.core.utils.safeDataCall
import kotlinx.coroutines.flow.Flow

interface RemoteRepository{
    suspend fun fetchNowPlayingMovie(): NowPlayingResponse

    suspend fun fetchPopularMovie(): PopularResponse

    suspend fun fetchUpcomingMovie(): UpComingResponse

    suspend fun fetchDetailMovie(movieId:Int?=null):DetailMovieResponse

    suspend fun fetchCreditMovie(movieId:Int?=null):CreditResponse

    suspend fun fetchSearchMovie(query:String): Flow<PagingData<DataSearch>>
}

class RemoteRepositoryImpl(
    private val remote : RemoteDataSource,
    private val paging: PagingDataSourceImpl
):RemoteRepository{
    override suspend fun fetchNowPlayingMovie(): NowPlayingResponse = safeDataCall{
        remote.fetchNowPlayingMovie()
    }

    override suspend fun fetchPopularMovie(): PopularResponse = safeDataCall{
        remote.fetchPopularMovie()
    }

    override suspend fun fetchUpcomingMovie(): UpComingResponse = safeDataCall{
        remote.fetchUpcomingMovie()
    }

    override suspend fun fetchDetailMovie(movieId: Int?): DetailMovieResponse = safeDataCall{
        remote.fetchDetailMovie(movieId)
    }

    override suspend fun fetchCreditMovie(movieId: Int?): CreditResponse = safeDataCall{
        remote.fetchCreditMovie(movieId)
    }

    override suspend fun fetchSearchMovie(query: String): Flow<PagingData<DataSearch>> = safeDataCall {
        paging.fetchSearchProduct(query)
    }

}