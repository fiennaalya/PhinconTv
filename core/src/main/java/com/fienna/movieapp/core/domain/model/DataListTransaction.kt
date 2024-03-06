package com.fienna.movieapp.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DataListTransaction (
    val listTransaction : List<DataTransaction> = listOf()
):Parcelable