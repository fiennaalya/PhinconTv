package com.fienna.movieapp.core.domain.usecase

import com.fienna.movieapp.core.domain.model.DataCart
import com.fienna.movieapp.core.domain.model.DataCredit
import com.fienna.movieapp.core.domain.model.DataDetailMovie
import com.fienna.movieapp.core.domain.model.DataNowPlaying
import com.fienna.movieapp.core.domain.model.DataPopular
import com.fienna.movieapp.core.domain.model.DataSession
import com.fienna.movieapp.core.domain.model.DataUpcoming
import com.fienna.movieapp.core.domain.model.DataUser
import com.fienna.movieapp.core.domain.model.DataWishlist
import com.fienna.movieapp.core.domain.state.UiState
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
    fun getProfileName():String
    fun putProfileName(value: String?)

    /*movie remote*/
    suspend fun fetchNowPlayingMovie(): List<DataNowPlaying>
    suspend fun fetchPopularMovie(): List<DataPopular>
    suspend fun fetchUpcomingMovie(): List<DataUpcoming>
    suspend fun fetchDetailMovie(movieId:Int):DataDetailMovie
    suspend fun fetchCreditMovie(movieId:Int):List<DataCredit>

    /*room database*/
    suspend fun fetchCart(userId:String): Flow<UiState<List<DataCart>>>
    suspend fun deleteAllCart()
    suspend fun insertCart(dataCart: DataCart?)
    suspend fun deleteCart(dataCart: DataCart)
    suspend fun checkAdd(movieId:Int) : Int


    suspend fun fetchWishlist(userId:String): Flow<UiState<List<DataWishlist>>>
    suspend fun deleteAllWishlist()
    suspend fun insertWishlist(dataWishlist: DataWishlist?)
    suspend fun deleteWishlist(dataWishlist: DataWishlist?)
    suspend fun checkFavorite(movieId:Int) : Int

}