package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import kotlinx.coroutines.runBlocking

class SearchViewModel(private val movieUsecase: MovieUsecase):ViewModel() {
    fun fetchSearchMovie(query:String) = runBlocking {
        movieUsecase.fetchSearchMovie(query)
    }
}