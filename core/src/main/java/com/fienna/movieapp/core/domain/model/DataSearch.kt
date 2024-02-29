package com.fienna.movieapp.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DataSearch (
    val id: Int? = null,
    val genreIds: List<Int>? = null,
    val popularity: Double? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val title: String? = null,
    val voteAverage: Double? = null ,
    val voteCount: Int? = null
): Parcelable