package com.fienna.movieapp.core.domain.usecase

import android.os.Bundle
import androidx.paging.PagingData
import com.fienna.movieapp.core.data.local.entity.CartEntity
import com.fienna.movieapp.core.data.local.entity.TransactionEntity
import com.fienna.movieapp.core.data.local.entity.WishlistEntity
import com.fienna.movieapp.core.data.remote.data.PaymentResponse
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
import com.fienna.movieapp.core.domain.repository.FirebaseRepository
import com.fienna.movieapp.core.domain.repository.PreLoginRepository
import com.fienna.movieapp.core.domain.repository.RemoteRepository
import com.fienna.movieapp.core.domain.repository.RoomRepository
import com.fienna.movieapp.core.domain.state.UiState
import com.fienna.movieapp.core.utils.DataMapper.toEntity
import com.fienna.movieapp.core.utils.DataMapper.toListData
import com.fienna.movieapp.core.utils.DataMapper.toUiData
import com.fienna.movieapp.core.utils.DataMapper.toUiListData
import com.fienna.movieapp.core.utils.safeDataCall
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MovieInteractor(
    private val firebaseRepository: FirebaseRepository,
    private val preLoginRepository: PreLoginRepository,
    private val remoteRepository: RemoteRepository,
    private val roomRepository: RoomRepository
) : MovieUsecase {
    override suspend fun signUp(email: String, password: String): Flow<Boolean> = safeDataCall {
        firebaseRepository.signUp(email, password)
    }

    override suspend fun signIn(email: String, password: String): Flow<Boolean> = safeDataCall {
        firebaseRepository.signIn(email, password)
    }

    override suspend fun updateProfile(userProfileChangeRequest: UserProfileChangeRequest): Flow<Boolean> =
        safeDataCall {
            firebaseRepository.updateProfile(userProfileChangeRequest)
        }

    override fun logScreenView(screenName: String) {
        firebaseRepository.logScreenView(screenName)
    }

    override fun logEvent(eventName: String, bundle: Bundle) {
        firebaseRepository.logEvent(eventName, bundle)
    }

    override suspend fun getConfigStatusToken(): Flow<Boolean> {
        return firebaseRepository.getConfigStatusToken()
    }

    override suspend fun getConfigToken(): Flow<List<DataToken>> =
        firebaseRepository.getConfigToken().map { config ->
            val typeToken = object : TypeToken<List<DataToken>>() {}.type
            val response: List<DataToken> = Gson().fromJson(config, typeToken)
            response
        }


    override suspend fun getConfigStatusPayment(): Flow<Boolean> {
        return firebaseRepository.getConfigStatusPayment()
    }

    override suspend fun getConfigPayment(): Flow<List<DataPayment>> =
        firebaseRepository.getConfigPayment().map { config ->
            val response = Gson().fromJson(config, PaymentResponse::class.java)
            val uiDataList = response.toListData()
            uiDataList
        }

    override fun getTokenValue(): Int  = preLoginRepository.getTokenValue()
    override fun putTokenValue(value: Int) { preLoginRepository.putTokenValue(value) }
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
        val triple: Triple<String?, String, Boolean> = Triple(userName, userId, onBoardingState)
        return triple.toUiData()
    }

    override fun getProfileName(): String = preLoginRepository.getProfileName()
    override fun putProfileName(value: String) {
        preLoginRepository.putProfileName(value)
    }

    override suspend fun fetchNowPlayingMovie(): List<DataNowPlaying> = safeDataCall {
        remoteRepository.fetchNowPlayingMovie().toUiListData()
    }

    override suspend fun fetchPopularMovie(): List<DataPopular> = safeDataCall {
        remoteRepository.fetchPopularMovie().toUiListData()
    }

    override suspend fun fetchUpcomingMovie(): List<DataUpcoming> = safeDataCall {
        remoteRepository.fetchUpcomingMovie().toUiListData()

    }

    override suspend fun fetchDetailMovie(movieId: Int): DataDetailMovie = safeDataCall {
        remoteRepository.fetchDetailMovie(movieId).toUiData()
    }

    override suspend fun fetchCreditMovie(movieId: Int): List<DataCredit> = safeDataCall {
        remoteRepository.fetchCreditMovie(movieId).toUiListData()
    }

    override suspend fun fetchSearchMovie(query: String): Flow<PagingData<DataSearch>> =
        safeDataCall {
            remoteRepository.fetchSearchMovie(query)
        }

    override suspend fun fetchCart(userId: String): Flow<UiState<List<DataCart>>> = safeDataCall {
        roomRepository.fetchCart(userId).map { data ->
            val mapped = data.map { entity: CartEntity -> entity.toUiData() }
            UiState.Success(mapped)
        }.flowOn(Dispatchers.IO).catch { throwable -> UiState.Error(throwable) }
    }

    override suspend fun deleteAllCart() {
        roomRepository.deleteAllCart()
    }

    override suspend fun insertCart(dataCart: DataCart?) {
        dataCart?.let { roomRepository.insertCart(it.toEntity()) }
    }

    override suspend fun deleteCart(dataCart: DataCart) {
        roomRepository.deleteCart(dataCart.toEntity())
    }

    override suspend fun checkAdd(movieId: Int): Int = safeDataCall {
        roomRepository.checkAdd(movieId)
    }

    override suspend fun fetchWishlist(userId: String): Flow<UiState<List<DataWishlist>>> =
        safeDataCall {
            roomRepository.fetchWishlist(userId).map { data ->
                val mapped = data.map { entity: WishlistEntity -> entity.toUiData() }
                UiState.Success(mapped)
            }.flowOn(Dispatchers.IO).catch { throwable -> UiState.Error(throwable) }
        }

    override suspend fun deleteAllWishlist() {
        roomRepository.deleteAllWishlist()
    }

    override suspend fun insertWishlist(dataWishlist: DataWishlist?) {
        dataWishlist?.let { roomRepository.insertWishlist(it.toEntity()) }
    }


    override suspend fun deleteWishlist(dataWishlist: DataWishlist?) {
        dataWishlist?.let { roomRepository.deleteWishlist(it.toEntity()) }
    }

    override suspend fun checkFavorite(movieId: Int): Int = safeDataCall {
        roomRepository.checkFavorite(movieId)
    }

    override suspend fun fetchAllTransaction(userId: String): Flow<UiState<List<DataTransaction>>> =
        safeDataCall {
            roomRepository.fetchAllTransaction(userId).map {data ->
                val mapped = data.map { entity: TransactionEntity -> entity.toUiData() }
                UiState.Success(mapped)
            }.flowOn(Dispatchers.IO).catch { throwable -> UiState.Error(throwable) }
        }

    override suspend fun insertTransaction(dataTransaction: DataTransaction?) {
        dataTransaction?.let { roomRepository.insertTransaction(it.toEntity()) }
    }

    override suspend fun checkTransaction(movieId: Int): Int = safeDataCall {
        roomRepository.checkTransaction(movieId)
    }

    override suspend fun fetchTransactionsForMovie(movieId: Int): Flow<UiState<DataTransaction>> =
        safeDataCall {
            roomRepository.fetchTransactionsForMovie(movieId).map {
                val data = it.toUiData()
                UiState.Success(data)
            }.flowOn(Dispatchers.IO).catch { throwable -> UiState.Error(throwable) }
        }

}