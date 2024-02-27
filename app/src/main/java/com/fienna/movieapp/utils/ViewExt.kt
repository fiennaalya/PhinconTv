package com.fienna.movieapp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import com.google.android.material.materialswitch.MaterialSwitch

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

fun changeGenre(genreId: Int): String {
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

fun extractYearFromDate(dateString: String): String {
    val year = dateString.split("-")[0]
    return year
}

fun formatRating(rating: Double): String {
    return String.format("%.1f", rating)
}