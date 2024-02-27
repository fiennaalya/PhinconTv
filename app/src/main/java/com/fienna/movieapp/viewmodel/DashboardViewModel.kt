package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fienna.movieapp.core.domain.model.DataUser
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import com.fienna.movieapp.view.dashboard.setting.SettingFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel(private val movieUsecase: MovieUsecase): ViewModel() {
    private  val _theme = MutableStateFlow(false)
    val theme = _theme.asStateFlow()

    fun getThemeValue() {
        _theme.update { movieUsecase.getSwitchThemeValue() }
    }
    fun saveThemeValue(value:Boolean){
        movieUsecase.putSwitchThemeValue(value)
    }
    fun getLanguageValue() : Boolean {
        return movieUsecase.getLanguageValue().equals(SettingFragment.languageIn, true)
    }
    fun saveLanguageValue(value:String){
        movieUsecase.putLanguageValue(value)
    }

    fun getCurrentUser(): DataUser? {
        return movieUsecase.getCurrentUser()
    }

    fun getUserId(): String{
        return movieUsecase.getUserId()
    }
    fun putUserId(id:String){
        movieUsecase.putUserId(id)
    }




}