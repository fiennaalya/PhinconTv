package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fienna.movieapp.core.domain.model.DataNowPlaying
import com.fienna.movieapp.core.domain.model.DataPopular
import com.fienna.movieapp.core.domain.model.DataUpcoming
import com.fienna.movieapp.core.domain.state.UiState
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import com.fienna.movieapp.core.utils.asMutableStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val movieUsecase: MovieUsecase): ViewModel() {
    private val _nowPlayingMovie: MutableStateFlow<UiState<List<DataNowPlaying>>> = MutableStateFlow(UiState.Empty)
    val nowPlayingMovie = _nowPlayingMovie.asStateFlow()

    private val _popularMovie: MutableStateFlow<UiState<List<DataPopular>>> = MutableStateFlow(UiState.Empty)
    val popularMovie = _popularMovie.asStateFlow()

    private val _upComingMovie: MutableStateFlow<UiState<List<DataUpcoming>>> = MutableStateFlow(UiState.Empty)
    val upComingMovie = _upComingMovie.asStateFlow()


    fun fetchNowPlayingMovie() {
        viewModelScope.launch {
            _nowPlayingMovie.asMutableStateFlow {
                movieUsecase.fetchNowPlayingMovie()
            }
        }
    }
    fun fetchPopularMovie(){
        viewModelScope.launch {
            _popularMovie.asMutableStateFlow {
                movieUsecase.fetchPopularMovie()
            }
        }
    }
   fun fetchUpcomingMovie(){
        viewModelScope.launch {
            _upComingMovie.asMutableStateFlow {
                movieUsecase.fetchUpcomingMovie()
            }
        }
   }

}