package com.fienna.movieapp.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DataToken (
    var paymentMethod:String = "",
    var price: Int = 0,
    var token:String = "",
    var transactionTime:String = "",
    var userId:String = "",
    var userName:String = "",
):Parcelable