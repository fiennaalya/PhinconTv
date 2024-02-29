package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import kotlinx.coroutines.runBlocking

class TokenViewModel(private val movieUsecase: MovieUsecase):ViewModel() {
    fun getConfigToken() = runBlocking {
        movieUsecase.getConfigToken()
    }

    fun getConfigStatusToken()= runBlocking {
        movieUsecase.getConfigStatusToken()
    }

    fun getConfigPayment()= runBlocking {
        movieUsecase.getConfigPayment()
    }

    fun getConfigStatusPayment()= runBlocking {
        movieUsecase.getConfigStatusPayment()
    }
}