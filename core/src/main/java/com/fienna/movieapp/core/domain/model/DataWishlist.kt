package com.fienna.movieapp.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DataWishlist (
    val wishlistId: Int = 0,
    val userId: String = "",
    val movieId: Int,
    val posterPath: String,
    val title: String,
    val popularity: Double,
    val voteAverage: Double,
    val voteCount: Int,
    val favorite: Boolean = false,
): Parcelable