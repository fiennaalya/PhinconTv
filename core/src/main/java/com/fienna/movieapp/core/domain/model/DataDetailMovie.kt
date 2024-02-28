package com.fienna.movieapp.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DataDetailMovie (
    val id: Int,
    val title: String,
    val backdropPath: String,
    val releaseDate: String,
    val genres: List<Genre>,
    val overview: String,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double
): Parcelable {
    @Keep
    @Parcelize
    data class Genre(
        val genreId: Int,
        val name: String
    ) : Parcelable
}