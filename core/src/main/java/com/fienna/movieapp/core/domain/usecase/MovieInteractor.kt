package com.fienna.movieapp.core.domain.usecase

import com.fienna.movieapp.core.domain.model.DataCredit
import com.fienna.movieapp.core.domain.model.DataDetailMovie
import com.fienna.movieapp.core.domain.model.DataNowPlaying
import com.fienna.movieapp.core.domain.model.DataPopular
import com.fienna.movieapp.core.domain.model.DataSession
import com.fienna.movieapp.core.domain.model.DataUpcoming
import com.fienna.movieapp.core.domain.model.DataUser
import com.fienna.movieapp.core.domain.repository.FirebaseRepository
import com.fienna.movieapp.core.domain.repository.PreLoginRepository
import com.fienna.movieapp.core.domain.repository.RemoteRepository
import com.fienna.movieapp.core.utils.DataMapper.toUiData
import com.fienna.movieapp.core.utils.DataMapper.toUiListData
import com.fienna.movieapp.core.utils.safeDataCall
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow

class MovieInteractor(
    private val firebaseRepository: FirebaseRepository,
    private val preLoginRepository: PreLoginRepository,
    private val remoteRepository: RemoteRepository
):MovieUsecase {
    override suspend fun signUp(email: String, password: String): Flow<Boolean> = safeDataCall{
        firebaseRepository.signUp(email,password)
    }

    override suspend fun signIn(email: String, password: String): Flow<Boolean> = safeDataCall{
        firebaseRepository.signIn(email, password)
    }

    override suspend fun updateProfile(userProfileChangeRequest: UserProfileChangeRequest): Flow<Boolean> = safeDataCall {
        firebaseRepository.updateProfile(userProfileChangeRequest)
    }

    override fun getCurrentUser(): DataUser? {
        return firebaseRepository.getCurrentUser()?.displayName?.let { DataUser(it) }
    }

    override fun getOnBoardingValue(): Boolean = preLoginRepository.getOnBoardingValue()

    override fun putOnBoardingValue(value: Boolean) {
        preLoginRepository.putOnBoardingValue(value)
    }

    override fun getSwitchThemeValue(): Boolean = preLoginRepository.getSwitchThemeValue()
    override fun putSwitchThemeValue(value: Boolean) {
        preLoginRepository.putSwitchThemeValue(value)
    }

    override fun getLanguageValue(): String = preLoginRepository.getLanguageValue()

    override fun putLanguageValue(value: String) {
        preLoginRepository.putLanguageValue(value)
    }

    override fun getUserId(): String = preLoginRepository.getUserId()


    override fun putUserId(id: String) {
        preLoginRepository.putUserId(id)
    }

    override fun getSessionData(): DataSession {
        val userName = firebaseRepository.getCurrentUser()?.displayName
        val userId = preLoginRepository.getUserId()
        val onBoardingState = preLoginRepository.getOnBoardingValue()
        val triple: Triple<String?,String, Boolean> = Triple(userName, userId, onBoardingState)
        return triple.toUiData()
    }

    override suspend fun fetchNowPlayingMovie(): List<DataNowPlaying> = safeDataCall{
        remoteRepository.fetchNowPlayingMovie().toUiListData()
    }

    override suspend fun fetchPopularMovie(): List<DataPopular> = safeDataCall{
        remoteRepository.fetchPopularMovie().toUiListData()
    }

    override suspend fun fetchUpcomingMovie(): List<DataUpcoming> = safeDataCall {
        remoteRepository.fetchUpcomingMovie().toUiListData()

    }

    override suspend fun fetchDetailMovie(movieId: Int): DataDetailMovie = safeDataCall{
        remoteRepository.fetchDetailMovie(movieId).toUiData()
    }

    override suspend fun fetchCreditMovie(movieId: Int): List<DataCredit> = safeDataCall{
        remoteRepository.fetchCreditMovie(movieId).toUiListData()
    }

}