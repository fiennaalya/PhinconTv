package com.fienna.movieapp.core.domain.usecase

import android.os.Bundle
import androidx.paging.PagingData
import com.fienna.movieapp.core.domain.model.DataCart
import com.fienna.movieapp.core.domain.model.DataCredit
import com.fienna.movieapp.core.domain.model.DataDetailMovie
import com.fienna.movieapp.core.domain.model.DataNowPlaying
import com.fienna.movieapp.core.domain.model.DataPayment
import com.fienna.movieapp.core.domain.model.DataPopular
import com.fienna.movieapp.core.domain.model.DataSearch
import com.fienna.movieapp.core.domain.model.DataSession
import com.fienna.movieapp.core.domain.model.DataToken
import com.fienna.movieapp.core.domain.model.DataTransaction
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
    fun logScreenView(screenName:String)
    fun logEvent(eventName:String, bundle: Bundle)

    /*firebase remote config*/
    suspend fun getConfigStatusToken():Flow<Boolean>
    suspend fun getConfigToken():Flow<List<DataToken>>
    suspend fun getConfigStatusPayment():Flow<Boolean>
    suspend fun getConfigPayment():Flow<List<DataPayment>>


    /*shared pref*/
    fun getTokenValue():Int
    fun putTokenValue(value:Int)
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
    fun putProfileName(value: String)

    /*movie remote*/
    suspend fun fetchNowPlayingMovie(): List<DataNowPlaying>
    suspend fun fetchPopularMovie(): List<DataPopular>
    suspend fun fetchUpcomingMovie(): List<DataUpcoming>
    suspend fun fetchDetailMovie(movieId:Int):DataDetailMovie
    suspend fun fetchCreditMovie(movieId:Int):List<DataCredit>
    suspend fun fetchSearchMovie(query:String): Flow<PagingData<DataSearch>>

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

    suspend fun fetchAllTransaction(userId:String): Flow<UiState<List<DataTransaction>>>
    suspend fun insertTransaction(dataTransaction: DataTransaction?)
    suspend fun checkTransaction(movieId:Int) : Int
    suspend fun fetchTransactionsForMovie(movieId: Int): Flow<UiState<DataTransaction>>

}