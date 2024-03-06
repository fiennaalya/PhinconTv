package com.fienna.movieapp.utils

object ValidationHelper {
    fun String?.validateEmail(): Boolean {
        val emailPattern =
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"
        val isValid = this?.let { emailPattern.toRegex().matches(it) }
        return isValid == true
    }

    fun String?.validatePassword(): Boolean {
        val isValid = this?.let { it.length >= 8 }
        return isValid == true
    }

    fun String.validateRequired(): Boolean = this.isNotEmpty() == true
}
