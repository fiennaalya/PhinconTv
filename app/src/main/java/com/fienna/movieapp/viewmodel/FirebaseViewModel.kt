package com.fienna.movieapp.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.fienna.movieapp.core.domain.usecase.MovieUsecase

class FirebaseViewModel(private val movieUsecase: MovieUsecase):ViewModel() {
    fun logScreenView(screenName:String){
        movieUsecase.logScreenView(screenName)
    }

    fun logEvent(eventName:String, bundle: Bundle){
        movieUsecase.logEvent(eventName, bundle)
    }
}