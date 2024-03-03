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

    val tncText = if (locale == "languageIn") "Ketentuan Layanan" else "Terms of Service"
    val startTnc = fullText.indexOf(tncText)
    val endTnc = startTnc + tncText.length

    val policyText = if (locale == "languageIn") "Kebijakan Privasi" else "Privacy Policy"
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

fun MaterialSwitch.checkIf(state: Boolean){
    this.isChecked = state
}

fun changeGenre(genreId: Int? = null): String {
    val genres = mapOf(
        28 to "Action",
        12 to "Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        10751 to "Family",
        14 to "Fantasy",
        36 to "History",
        27 to "Horror",
        10402 to "Music",
        9648 to "Mystery",
        10749 to "Romance",
        878 to "Science Fiction",
        10770 to "TV Movie",
        53 to "Thriller",
        10752 to "War",
        37 to "Western"
    )

    return genres[genreId] ?: "Unknown Genre"
}

fun extractYearFromDate(dateString: String? = null): String {
    val date = dateString ?: ""  // Using the elvis operator to handle null or empty cases
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

fun currency(number : Int): String{
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
        e.printStackTrace()
        return ""
    }
}