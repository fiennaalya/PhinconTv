package com.fienna.movieapp.core.domain.usecase

import com.fienna.movieapp.core.domain.model.DataCredit
import com.fienna.movieapp.core.domain.model.DataDetailMovie
import com.fienna.movieapp.core.domain.model.DataNowPlaying
import com.fienna.movieapp.core.domain.model.DataPopular
import com.fienna.movieapp.core.domain.model.DataSession
import com.fienna.movieapp.core.domain.model.DataUpcoming
import com.fienna.movieapp.core.domain.model.DataUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow

interface MovieUsecase {
    /*firebase*/
    suspend fun signUp(email:String, password:String): Flow<Boolean>
    suspend fun signIn(email:String, password:String): Flow<Boolean>
    suspend fun updateProfile(userProfileChangeRequest: UserProfileChangeRequest): Flow<Boolean>

    /*shared pref*/
    fun getCurrentUser(): DataUser?
    fun getOnBoardingValue():Boolean
    fun putOnBoardingValue(value:Boolean)
    fun getSwitchThemeValue():Boolean
    fun putSwitchThemeValue(value:Boolean)
    fun getLanguageValue():String
    fun putLanguageValue(value: String)
    fun getUserId(): String
    fun putUserId(id:String)
    fun getSessionData(): DataSession

    /*movie remote*/
    suspend fun fetchNowPlayingMovie(): List<DataNowPlaying>
    suspend fun fetchPopularMovie(): List<DataPopular>
    suspend fun fetchUpcomingMovie(): List<DataUpcoming>
    suspend fun fetchDetailMovie(movieId:Int):DataDetailMovie
    suspend fun fetchCreditMovie(movieId:Int):List<DataCredit>
}