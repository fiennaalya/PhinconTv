package com.fienna.movieapp.core.data.remote.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fienna.movieapp.core.data.remote.service.ApiEndPoint
import com.fienna.movieapp.core.domain.model.DataSearch
import kotlinx.coroutines.flow.Flow

class PagingDataSourceImpl(
    private val apiEndPoint: ApiEndPoint
) {
    fun fetchSearchProduct(query : String): Flow<PagingData<DataSearch>>{
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = { PagingDataSource(endpoint =apiEndPoint, query = query) }
        ).flow
    }
}