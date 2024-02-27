package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fienna.movieapp.core.domain.model.DataSession
import com.fienna.movieapp.core.domain.state.SplashState
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import com.fienna.movieapp.core.utils.DataMapper.toSplashState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PreLoginViewModel(private val useCase: MovieUsecase) :ViewModel(){
    private val _onBoarding = MutableStateFlow<SplashState<DataSession>>(SplashState.OnBoarding)
    val onBoarding = _onBoarding.asStateFlow()

    fun getOnBoardingValue(){
        _onBoarding.update { useCase.getSessionData().toSplashState() }
    }

    fun saveOnBoardingValue(value:Boolean){
        useCase.putOnBoardingValue(value)
    }
}