package com.fienna.movieapp.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DataCart(
    val cartId: Int = 0,
    val userId: String = "",
    val movieId: Int,
    val posterPath: String,
    val title: String,
    val genreName: String,
    val popularity: Double,
    val isChecked: Boolean = false
):Parcelable
