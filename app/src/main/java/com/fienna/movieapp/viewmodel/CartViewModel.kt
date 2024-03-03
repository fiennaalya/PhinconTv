package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fienna.movieapp.core.domain.model.DataCart
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CartViewModel(private val movieUsecase: MovieUsecase): ViewModel() {
    private val _totalPrice = MutableStateFlow(0)
    val totalPrice = _totalPrice.asStateFlow()

    fun fetchCart()= runBlocking {
        val userId = movieUsecase.getUserId()
        movieUsecase.fetchCart(userId)
    }

    fun deleteCart(data: DataCart){
        viewModelScope.launch{
            movieUsecase.deleteCart(data)
        }
    }

    fun totalPrice(){
        viewModelScope.launch {
            var updatedTotalPrice = 0
            updatedTotalPrice = movieUsecase.updateTotalPriceChecked() ?: 0
            _totalPrice.value = updatedTotalPrice
        }
    }

    fun updateCheckCart(cartId:Int, value:Boolean){
        viewModelScope.launch {
            movieUsecase.updateCheckCart(cartId, value)
        }
    }
}