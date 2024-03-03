package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fienna.movieapp.core.domain.model.DataMovieTransaction
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class TransactionViewModel(private val movieUsecase: MovieUsecase):ViewModel() {

}