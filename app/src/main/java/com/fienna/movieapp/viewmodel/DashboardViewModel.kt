package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fienna.movieapp.core.domain.model.DataMovieTransaction
import com.fienna.movieapp.core.domain.model.DataUser
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import com.fienna.movieapp.view.dashboard.setting.SettingFragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

class DashboardViewModel(private val movieUsecase: MovieUsecase) : ViewModel() {
    private val _theme = MutableStateFlow(false)
    val theme = _theme.asStateFlow()

    private val _profileUserName = MutableStateFlow("")
    val profileUserName = _profileUserName.asStateFlow()

    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    fun getThemeValue() {
        _theme.update { movieUsecase.getSwitchThemeValue() }
    }

    fun saveThemeValue(value: Boolean) {
        movieUsecase.putSwitchThemeValue(value)
    }

    fun getLanguageValue(): Boolean {
        return movieUsecase.getLanguageValue().equals(SettingFragment.languageIn, true)
    }

    fun saveLanguageValue(value: String) {
        movieUsecase.putLanguageValue(value)
    }

    fun getCurrentUser(): DataUser? {
        return movieUsecase.getCurrentUser()
    }

    fun getUserId() {
        _userId.update { movieUsecase.getUserId() }
    }

    fun getProfileName() {
        _profileUserName.update { movieUsecase.getProfileName() }
    }

    fun getTokenFromFirebase(userId: String): Flow<Int> = runBlocking {
        movieUsecase.getTokenFromFirebase(userId)
    }

    fun sendMovieToDatabase(
        dataMovieTransaction: DataMovieTransaction,
        userId: String,
        movieId: String
    ): Flow<Boolean> = runBlocking {
        movieUsecase.sendMovieToDatabase(dataMovieTransaction, userId, movieId)
    }

    fun getMovieTokenFromFirebase(userId: String): Flow<Int> = runBlocking {
        movieUsecase.getMovieTokenFromFirebase(userId)
    }

    fun getMovieFromDatabase(userId: String, movieId: String): Flow<DataMovieTransaction?> =
        runBlocking {
            movieUsecase.getMovieFromFirebase(userId, movieId)
        }

    fun getAllMovieFromDatabase(userId: String): Flow<List<DataMovieTransaction>> = runBlocking {
        movieUsecase.getAllMovieFromFirebase(userId)
    }
}
