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
import com.fienna.movieapp.utils.toBase64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailViewModel(private val movieUsecase: MovieUsecase):ViewModel() {
    private val _detailMovie: MutableStateFlow<UiState<DataDetailMovie>> = MutableStateFlow(UiState.Empty)
    val detailMovie = _detailMovie.asStateFlow()

    private val _creditMovie: MutableStateFlow<UiState<List<DataCredit>>> = MutableStateFlow(UiState.Empty)
    val creditMovie = _creditMovie.asStateFlow()

    private var dataCart: DataCart? = null
    private var dataWishlist: DataWishlist? = null
    fun fetchDetailMovie(movieId:Int){
        viewModelScope.launch {
            _detailMovie.asMutableStateFlow {
                movieUsecase.fetchDetailMovie(movieId)
            }
        }
    }

    fun fetchCreditMovie(movieId:Int){
        viewModelScope.launch {
            _creditMovie.asMutableStateFlow {
                movieUsecase.fetchCreditMovie(movieId)
            }
        }
    }

    fun setDataCart(data: DataCart){
        dataCart = data
    }
    fun setDataWishlist(data: DataWishlist){
        dataWishlist = data
    }

    fun insertWishlist() = runBlocking{
        val username = movieUsecase.getProfileName()
        dataWishlist = dataWishlist?.copy(userId = username.toBase64())
        movieUsecase.insertWishlist(dataWishlist)
        println("MASUK VIEWMODEL DATA WISHLIST  $dataWishlist")
    }


    fun insertCart() = runBlocking{
        val username = movieUsecase.getProfileName()
        dataCart = dataCart?.copy(userId = username.toBase64())
        movieUsecase.insertCart(dataCart)
    }

    fun checkFavorite(movieId: Int) = runBlocking {
        movieUsecase.checkFavorite(movieId) > 0
    }

    fun resetFavoriteValue(movieId: Int) = runBlocking {
        movieUsecase.checkFavorite(movieId) == 1
    }

    fun checkAdd(movieId: Int) = runBlocking {
        movieUsecase.checkAdd(movieId) > 0
    }

}