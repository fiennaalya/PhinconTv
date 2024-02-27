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