package com.fienna.movieapp.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fienna.movieapp.core.utils.MovieConstant

@Entity(tableName = MovieConstant.tableWishlist)
data class WishlistEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="wishlist_id")
    val wishlistId: Int = 0,
    @ColumnInfo(name = "user_id")
    val userId: String = "",
    @ColumnInfo("movie_id")
    val movieId: Int,
    @ColumnInfo("poster_path")
    val posterPath: String,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("popularity")
    val popularity: Double,
    @ColumnInfo("vote_average")
    val voteAverage: Double,
    @ColumnInfo("vote_count")
    val voteCount: Int,
    @ColumnInfo(name = "favorite")
    val favorite: Boolean = false,
)