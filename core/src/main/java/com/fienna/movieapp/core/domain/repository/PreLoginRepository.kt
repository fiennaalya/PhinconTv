package com.fienna.movieapp.core.domain.repository

import com.fienna.movieapp.core.data.local.datasource.LocalDataSource

interface PreLoginRepository{
    fun getOnBoardingValue():Boolean
    fun putOnBoardingValue(value:Boolean)
    fun getSwitchThemeValue():Boolean
    fun putSwitchThemeValue(value:Boolean)
    fun getLanguageValue():String
    fun putLanguageValue(value: String)
    fun getUserId(): String
    fun putUserId(id:String)
}

class PreLoginRepositoryImpl(
    private val local:LocalDataSource
):PreLoginRepository{
    override fun getOnBoardingValue(): Boolean = local.getOnBoardingValue() == true
    override fun putOnBoardingValue(value: Boolean) { local.putOnBoardingValue(value) }
    override fun getSwitchThemeValue(): Boolean = local.getSwitchThemeValue() == true
    override fun putSwitchThemeValue(value: Boolean) { local.putSwitchThemeValue(value) }
    override fun getLanguageValue(): String = local.getLanguageValue().toString()
    override fun putLanguageValue(value: String) { local.putLanguageValue(value) }
    override fun getUserId(): String = local.getUserId().toString()
    override fun putUserId(id: String) { local.putUserId(id) }
}