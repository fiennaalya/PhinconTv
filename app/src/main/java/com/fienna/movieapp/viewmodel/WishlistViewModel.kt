package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fienna.movieapp.core.domain.model.DataWishlist
import com.fienna.movieapp.core.domain.repository.FirebaseRepository
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WishlistViewModel(
    private val movieUsecase: MovieUsecase,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _countWishlist = MutableStateFlow(0)
    val countWishlist = _countWishlist.asStateFlow()

    private val _badge = MutableSharedFlow<Int>(0)
    val badge = _badge.asSharedFlow()

    private val _totalCountWishlist = MutableStateFlow(0)
    val totalCountWishlist = _totalCountWishlist.asStateFlow()

    fun fetchWishlist() = runBlocking {
        val userId = movieUsecase.getUserId()
        movieUsecase.fetchWishlist(userId)
    }


    fun deleteWishlist(data: DataWishlist) {
        viewModelScope.launch {
            println("MASUK DATA DELETE $data")

            movieUsecase.deleteWishlist(data)
            println("MASUK DATA AFTER DELETE $data")
            countWishlist()
        }
    }

    fun countWishlist() {
        viewModelScope.launch {
            val userId = movieUsecase.getUserId()
            _countWishlist.update { movieUsecase.countWishlist(userId) }
        }
    }
}
