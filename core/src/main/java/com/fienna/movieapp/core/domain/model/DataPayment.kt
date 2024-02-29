package com.fienna.movieapp.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DataPayment(
    var item: List<Item> = listOf(),
    var title: String
): Parcelable {
    @Keep
    @Parcelize
    data class Item(
        var image: String,
        var label: String,
        var status: Boolean
    ): Parcelable
}