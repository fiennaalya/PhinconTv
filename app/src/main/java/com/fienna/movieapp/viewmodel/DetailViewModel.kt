package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fienna.movieapp.core.domain.model.DataCart
import com.fienna.movieapp.core.domain.model.DataCredit
import com.fienna.movieapp.core.domain.model.DataDetailMovie
import com.fienna.movieapp.core.domain.model.DataWishlist
import com.fienna.movieapp.core.domain.state.UiState
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import com.fienna.movieapp.core.utils.asMutableStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailViewModel(private val movieUsecase: MovieUsecase) : ViewModel() {
    private val _detailMovie: MutableStateFlow<UiState<DataDetailMovie>> =
        MutableStateFlow(UiState.Empty)
    val detailMovie = _detailMovie.asStateFlow()

    private val _creditMovie: MutableStateFlow<UiState<List<DataCredit>>> =
        MutableStateFlow(UiState.Empty)
    val creditMovie = _creditMovie.asStateFlow()

    private var dataCart: DataCart? = null
    private var dataWishlist: DataWishlist? = null


    fun fetchDetailMovie(movieId: Int) {
        viewModelScope.launch {
            _detailMovie.asMutableStateFlow {
                movieUsecase.fetchDetailMovie(movieId)
            }
        }
    }

    fun fetchCreditMovie(movieId: Int) {
        viewModelScope.launch {
            _creditMovie.asMutableStateFlow {
                movieUsecase.fetchCreditMovie(movieId)
            }
        }
    }

    fun setDataCart(data: DataCart) {
        dataCart = data
    }

    fun setDataWishlist(data: DataWishlist) {
        dataWishlist = data
    }

    fun insertWishlist() = runBlocking {
        val userId = movieUsecase.getUserId()
        dataWishlist = dataWishlist?.copy(userId = userId)
        movieUsecase.insertWishlist(dataWishlist)
    }


    fun insertCart() = runBlocking {
        val userId = movieUsecase.getUserId()
        dataCart = dataCart?.copy(userId = userId)
        movieUsecase.insertCart(dataCart)
    }

    fun deleteCartDetail() {
        viewModelScope.launch {
            val userId = movieUsecase.getUserId()
            dataCart?.let {
                val movieData = movieUsecase.fetchMovieCart(it.movieId, userId)
                movieUsecase.deleteCart(movieData)
            }
        }
    }

    fun checkFavorite(movieId: Int): Int = runBlocking {
        val userId = movieUsecase.getUserId()
        movieUsecase.checkFavorite(movieId, userId)
    }

    fun checkAdd(movieId: Int): Int = runBlocking {
        val userId = movieUsecase.getUserId()
        movieUsecase.checkAdd(movieId, userId)
    }

    fun deleteWishlistDetail() {
        val userId = movieUsecase.getUserId()
        viewModelScope.launch {
            dataWishlist?.let {
                val movieData = movieUsecase.fetchMovieWishlist(it.movieId, userId)
                movieUsecase.deleteWishlist(movieData)
            }
        }
    }


}
