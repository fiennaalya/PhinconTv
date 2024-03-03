package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fienna.movieapp.core.domain.model.DataToken
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking

class TokenViewModel(private val movieUsecase: MovieUsecase):ViewModel() {
    private val _selectedToken = MutableStateFlow(0)
    val selectedToken = _selectedToken.asStateFlow()

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

    fun sendDataToDatabase(dataToken: DataToken, userId: String): Flow<Boolean> = runBlocking {
        movieUsecase.sendDataToDatabase(dataToken, userId)
    }

}