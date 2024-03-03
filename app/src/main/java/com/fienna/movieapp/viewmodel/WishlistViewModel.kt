package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fienna.movieapp.core.domain.model.DataWishlist
import com.fienna.movieapp.core.domain.repository.FirebaseRepository
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WishlistViewModel(private val movieUsecase: MovieUsecase, private val firebaseRepository: FirebaseRepository):ViewModel() {
    private val _countWishlist = MutableSharedFlow<Int>(0)
    val countWishlist = _countWishlist.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    private val _badge = MutableSharedFlow<Int>(0)
    val badge = _badge.asSharedFlow()

    private val _totalCountWishlist = MutableStateFlow(0)
    val totalCountWishlist = _totalCountWishlist.asStateFlow()

    fun fetchWishlist()= runBlocking {
        val userId = movieUsecase.getUserId()
        movieUsecase.fetchWishlist(userId)
    }


    fun deleteWishlist(data: DataWishlist){
        viewModelScope.launch{
            movieUsecase.deleteWishlist(data)
            setBadge()
        }
    }
//    fun countWishlist() {
//        viewModelScope.launch {
//            val userId = movieUsecase.getUserId()
//            val count = movieUsecase.countWishlist(userId)
//            _countWishlist.emit(count)
//            setbadge(count)
//            println("masuk count viewmodel: $count")
//        }
//    }

    fun setBadge(){
        viewModelScope.launch {
            val userId = movieUsecase.getUserId()
            val count = movieUsecase.countWishlist(userId)
            println("masuk viewmodel $count")
            _badge.emit(count)
        }
    }
}