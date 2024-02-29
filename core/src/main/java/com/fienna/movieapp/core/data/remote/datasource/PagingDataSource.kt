package com.fienna.movieapp.core.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fienna.movieapp.core.data.remote.service.ApiEndPoint
import com.fienna.movieapp.core.domain.model.DataSearch
import com.fienna.movieapp.core.utils.DataMapper.toUiData
import retrofit2.HttpException
import java.io.IOException

class PagingDataSource (
    private val endpoint : ApiEndPoint,
    private val query: String
):PagingSource<Int, DataSearch>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataSearch> {
        return try{
            val pageIndex = params.key ?: INITIAL_PAGE_INDEX
            val response = endpoint.fetchSearchMovie(query, pageIndex)
            val movies = response.results.map {it.toUiData() }.toList()
            LoadResult.Page(
                data = movies,
                prevKey = if (pageIndex == INITIAL_PAGE_INDEX) null else pageIndex -1,
                nextKey = if (movies.isEmpty()) null else pageIndex+1
            )
        }catch (exception : IOException){
            return LoadResult.Error(exception)
        }catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DataSearch>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object{
        const val INITIAL_PAGE_INDEX = 1

    }

}
