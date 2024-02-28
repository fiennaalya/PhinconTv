package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fienna.movieapp.core.domain.model.DataCart
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import com.fienna.movieapp.utils.toBase64
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CartViewModel(private val movieUsecase: MovieUsecase): ViewModel() {
    fun fetchCart()= runBlocking {
        val username = movieUsecase.getProfileName()
        movieUsecase.fetchCart(username.toBase64())
    }


    fun deleteCart(data: DataCart){
        viewModelScope.launch{
            movieUsecase.deleteCart(data)
        }
    }
}