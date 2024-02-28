package com.fienna.movieapp.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DataNowPlaying (
    val releaseDate: String,
    val backdropPath: String,
    val posterPath: String,
    val id: Int,
    val title: String,
    val genreIds: List<Int>,
    val voteAverage: Double,
    val voteCount: Int
):Parcelable