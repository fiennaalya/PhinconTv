package com.fienna.movieapp.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fienna.movieapp.core.utils.MovieConstant

@Entity(tableName = MovieConstant.tableTransaction)
data class TransactionEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    val transactionId: Int = 0,
    @ColumnInfo(name = "user_id")
    val userId: String = "",
    @ColumnInfo("movie_id")
    val movieId: Int,
    @ColumnInfo("poster_path")
    val posterPath: String,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("genre_name")
    val genreName: String,
    @ColumnInfo("popularity")
    val popularity: Double
)