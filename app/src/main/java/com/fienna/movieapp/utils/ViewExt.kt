package com.fienna.movieapp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import com.fienna.movieapp.R
import com.google.android.material.materialswitch.MaterialSwitch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Base64
import java.util.Date
import java.util.Locale

fun setText(locale: String, context: Context, textInput: TextView, fullText: String) {
    val spannable = SpannableString(fullText)

    val tncText = if (locale == "in") "Ketentuan Layanan" else "Terms of Service"
    val startTnc = fullText.indexOf(tncText)
    val endTnc = startTnc + tncText.length

    val policyText = if (locale == "in") "Kebijakan Privasi" else "Privacy Policy"
    val startPolicy = fullText.indexOf(policyText)
    val endPolicy = startPolicy + policyText.length

    val tncClickableSpan = object : ClickableSpan() {
        override fun onClick(p0: View) {
            openUrl(context, "https://phincon.com/")
        }
    }

    val policyClickableSpan = object : ClickableSpan() {
        override fun onClick(p0: View) {
            openUrl(context, "https://google.com/")
        }
    }

    spannable.setSpan(
        ForegroundColorSpan(Color.parseColor("#6750A4")),
        startTnc,
        endTnc,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(
        ForegroundColorSpan(Color.parseColor("#6750A4")),
        startPolicy,
        endPolicy,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    spannable.setSpan(
        tncClickableSpan,
        startTnc,
        endTnc,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(
        policyClickableSpan,
        startPolicy,
        endPolicy,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    textInput.text = spannable
    textInput.movementMethod = LinkMovementMethod.getInstance()
}

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

fun MaterialSwitch.checkIf(state: Boolean) {
    this.isChecked = state
}

fun changeGenre(genreId: Int? = null, context: Context): String {
    val genres = mapOf(
        28 to context.getString(R.string.genre_action),
        12 to context.getString(R.string.genre_adventure),
        16 to context.getString(R.string.genre_animation),
        35 to context.getString(R.string.genre_comedy),
        80 to context.getString(R.string.genre_crime),
        99 to context.getString(R.string.genre_documentary),
        18 to context.getString(R.string.genre_drama),
        10751 to context.getString(R.string.genre_family),
        14 to context.getString(R.string.genre_fantasy),
        36 to context.getString(R.string.genre_history),
        27 to context.getString(R.string.genre_horror),
        10402 to context.getString(R.string.genre_music),
        9648 to context.getString(R.string.genre_mystery),
        10749 to context.getString(R.string.genre_romance),
        878 to context.getString(R.string.genre_science_fiction),
        10770 to context.getString(R.string.genre_tv_movie),
        53 to context.getString(R.string.genre_thriller),
        10752 to context.getString(R.string.genre_war),
        37 to context.getString(R.string.genre_western)
    )
    return genres[genreId] ?: context.getString(R.string.unknown_genre)
}

fun extractYearFromDate(dateString: String? = null): String {
    val date = dateString ?: ""
    val year = if (date.isNotEmpty()) {
        date.split("-")[0]
    } else {
        ""
    }
    return year
}

fun formatRating(rating: Double? = null): String {
    return String.format("%.1f", rating)
}

fun String.toBase64(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Base64.getEncoder().encodeToString(this.toByteArray())
    } else {
        android.util.Base64.encodeToString(this.toByteArray(), android.util.Base64.DEFAULT)
    }
}

fun currency(number: Int): String {
    val currencyIn = DecimalFormat.getCurrencyInstance() as DecimalFormat
    val formatRupiah = DecimalFormatSymbols()

    formatRupiah.currencySymbol = "Rp "
    formatRupiah.groupingSeparator = '.'

    currencyIn.decimalFormatSymbols = formatRupiah
    return currencyIn.format(number).dropLast(3)
}


fun DateTimeNow(): String {
    val dateTime: String

    dateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val localDateTime = LocalDateTime.now()
        localDateTime.format(formatter)
    } else {
        val legacyFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val legacyDateTime = legacyFormatter.format(Date())
        legacyDateTime
    }

    return dateTime
}

fun extractYearMonthDate(dateTimeString: String?): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    try {
        val date = inputFormat.parse(dateTimeString)
        return outputFormat.format(date)
    } catch (e: ParseException) {
        return ""
    }
}
