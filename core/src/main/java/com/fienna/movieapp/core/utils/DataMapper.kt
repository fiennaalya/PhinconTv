package com.fienna.movieapp.core.utils

import com.fienna.movieapp.core.domain.model.DataSession
import com.fienna.movieapp.core.domain.state.SplashState

object DataMapper {
    fun Triple<String?, String, Boolean>.toUiData() = DataSession(
        userName = this.first,
        userId = this.second,
        onBoardingState = this.third
    )

    fun DataSession.toSplashState() = when{
        this.userName?.isEmpty() == true && this.userId.isNotEmpty() ->{
            SplashState.Profile
        }
        this.userName?.isNotEmpty() == true && this.userId.isNotEmpty() -> {
            SplashState.Dashboard
        }
        this.onBoardingState -> {
            SplashState.Login
        }
        else -> {
            SplashState.OnBoarding
        }
    }
}