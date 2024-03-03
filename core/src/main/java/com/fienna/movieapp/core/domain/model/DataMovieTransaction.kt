package com.fienna.movieapp.core.domain.model

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class DataMovieTransaction (
    var movieId: Int = 0,
    var priceMovie: Int =0,
    var titleMovie :String = "",
    var transactionTime:String = "",
    var userId: String = "",
    var userName:String = ""
):Parcelable{
    override fun toString(): String {
        return "DataMovieTransaction(movieId=$movieId, userId=$userId, userName=$userName, titleMovie=$titleMovie, priceMovie=$priceMovie, transactionTime=$transactionTime)"
    }
}