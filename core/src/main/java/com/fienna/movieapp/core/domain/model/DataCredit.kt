package com.fienna.movieapp.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DataCredit (
    val character: String? ="",
    val name: String? = "",
    val profilePath: String? =""
):Parcelable