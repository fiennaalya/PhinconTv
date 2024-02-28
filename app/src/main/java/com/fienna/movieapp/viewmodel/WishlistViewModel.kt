package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fienna.movieapp.core.domain.model.DataWishlist
import com.fienna.movieapp.core.domain.repository.FirebaseRepository
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import com.fienna.movieapp.utils.toBase64
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WishlistViewModel(private val movieUsecase: MovieUsecase, private val firebaseRepository: FirebaseRepository):ViewModel() {

    fun fetchWishlist()= runBlocking {
        val username = movieUsecase.getProfileName()
        movieUsecase.fetchWishlist(username.toBase64())
    }


    fun deleteWishlist(data: DataWishlist){
        viewModelScope.launch{
            movieUsecase.deleteWishlist(data)
        }
    }
}