package com.fienna.movieapp.core.data.local.preferences

import android.content.Context
import com.fienna.movieapp.core.utils.MovieConstant

interface SharedPref{
    fun getSwitchThemeValue():Boolean?
    fun putSwitchThemeValue(value:Boolean)
    fun getLanguageValue():String?
    fun putLanguageValue(value: String)
    fun getOnBoardingValue():Boolean?
    fun putOnBoardingValue(value:Boolean)
    fun getProfileName():String?
    fun putProfileName(value: String?)
    fun getUserId(): String?
    fun putUserId(id:String)

}

class SharedPrefImpl (private val context: Context):SharedPref{
    private val prefs = context.getSharedPreferences(MovieConstant.prefName, Context.MODE_PRIVATE)
    override fun getSwitchThemeValue(): Boolean? = prefs.getBoolean(MovieConstant.themeKey, false)

    override fun putSwitchThemeValue(value: Boolean) {
        prefs.edit().apply(){
            putBoolean(MovieConstant.themeKey,value)
            apply()
        }
    }

    override fun getLanguageValue(): String? = prefs.getString(MovieConstant.langKey, "")

    override fun putLanguageValue(value: String) {
        prefs.edit().apply{
            putString(MovieConstant.langKey, value)
            apply()
        }
    }

    override fun getOnBoardingValue(): Boolean = prefs.getBoolean(MovieConstant.onBoardingKey, false)
    override fun putOnBoardingValue(value: Boolean) {
        prefs.edit().apply(){
            putBoolean(MovieConstant.onBoardingKey,value)
            apply()
        }
    }

    override fun getProfileName(): String? = prefs.getString(MovieConstant.profileName, "")

    override fun putProfileName(value: String?) {
        prefs.edit().apply{
            putString(MovieConstant.profileName, value)
            apply()
        }
    }

    override fun getUserId(): String? = prefs.getString(MovieConstant.userIdFirebase, "")

    override fun putUserId(id: String) {
        prefs.edit().apply {
            putString(MovieConstant.userIdFirebase, id)
            apply()
        }
    }

}