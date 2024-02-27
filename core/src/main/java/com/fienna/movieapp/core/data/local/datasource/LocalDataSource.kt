package com.fienna.movieapp.core.data.local.datasource

import com.fienna.movieapp.core.data.local.preferences.SharedPref

class LocalDataSource(
    private val sharedPref: SharedPref
) {
    fun getOnBoardingValue(): Boolean?= sharedPref.getOnBoardingValue()
    fun putOnBoardingValue(value:Boolean){
        sharedPref.putOnBoardingValue(value)
    }
    fun getSwitchThemeValue():Boolean? = sharedPref.getSwitchThemeValue()
    fun putSwitchThemeValue(value:Boolean){
        sharedPref.putSwitchThemeValue(value)
    }
    fun getLanguageValue(): String? = sharedPref.getLanguageValue()
    fun putLanguageValue(value: String){
        sharedPref.putLanguageValue(value)
    }

    fun getUserId(): String? = sharedPref.getUserId()
    fun putUserId(id:String){
        sharedPref.putUserId(id)
    }
}