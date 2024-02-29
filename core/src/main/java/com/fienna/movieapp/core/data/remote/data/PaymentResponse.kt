package com.fienna.movieapp.core.data.remote.data

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class PaymentResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: List<Data> = listOf(),
    @SerializedName("message")
    val message: String
) : Parcelable {
    @Keep
    @Parcelize
    data class Data(
        @SerializedName("item")
        val item: List<Item> = listOf(),
        @SerializedName("title")
        val title: String
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Item(
            @SerializedName("image")
            val image: String,
            @SerializedName("label")
            val label: String,
            @SerializedName("status")
            val status: Boolean
        ) : Parcelable
    }
}