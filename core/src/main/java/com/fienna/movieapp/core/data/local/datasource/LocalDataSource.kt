package com.fienna.movieapp.core.data.local.datasource

import com.fienna.movieapp.core.data.local.database.MovieDao
import com.fienna.movieapp.core.data.local.entity.CartEntity
import com.fienna.movieapp.core.data.local.entity.TransactionEntity
import com.fienna.movieapp.core.data.local.entity.WishlistEntity
import com.fienna.movieapp.core.data.local.preferences.SharedPref
import com.fienna.movieapp.core.utils.safeDataCall
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val sharedPref: SharedPref,
    private val dao: MovieDao
) {
    fun getTokenValue():Int = sharedPref.getTokenValue()
    fun putTokenValue(value:Int){sharedPref.putTokenValue(value)}
    fun getOnBoardingValue(): Boolean?= sharedPref.getOnBoardingValue()
    fun putOnBoardingValue(value:Boolean){
        sharedPref.putOnBoardingValue(value)
    }
    fun getSwitchThemeValue():Boolean? = sharedPref.getSwitchThemeValue()
    fun putSwitchThemeValue(value:Boolean){
        sharedPref.putSwitchThemeValue(value)
    }
    fun getLanguageValue(): String? = sharedPref.getLanguageValue()
    fun putLanguageValue(value: String){
        sharedPref.putLanguageValue(value)
    }

    fun getUserId(): String? = sharedPref.getUserId()
    fun putUserId(id:String){
        sharedPref.putUserId(id)
    }

    fun getProfileName():String? =sharedPref.getProfileName()
    fun putProfileName(value:String?) {
        sharedPref.putProfileName(value)
    }

    /*room database code*/
    fun fetchCart(userId:String): Flow<List<CartEntity>> = dao.retrieveAllCart(userId)
    suspend fun deleteAllCart() = dao.deleteAllCart()
    suspend fun insertCart(cartEntity: CartEntity) { dao.insertCart(cartEntity) }
    suspend fun deleteCart(cartEntity: CartEntity){dao.deleteCart(cartEntity)}
    suspend fun checkAdd(movieId: Int) : Int = safeDataCall { dao.checkAdd(movieId) }

    fun fetchWishlist(userId:String): Flow<List<WishlistEntity>> = dao.retrieveAllWishlist(userId)
    suspend fun deleteAllWishlist() = dao.deleteAllWishlist()
    suspend fun insertWishlist(wishlistEntity: WishlistEntity) { dao.insertWishlist(wishlistEntity) }
    suspend fun deleteWishlist(wishlistEntity: WishlistEntity){dao.deleteWishlist(wishlistEntity)}
    suspend fun checkFavorite(movieId: Int) : Int = safeDataCall { dao.checkFavorite(movieId) }


    fun fetchAllTransaction(userId:String): Flow<List<TransactionEntity>> = dao.retrieveAllTransaction(userId)
    suspend fun insertTransaction(transactionEntity: TransactionEntity) { dao.insertTransaction(transactionEntity) }
    suspend fun retrieveTransactionsForMovie(movieId: Int):Flow<TransactionEntity> = dao.retrieveTransactionsForMovie(movieId)
    suspend fun checkTransaction(movieId: Int) : Int = safeDataCall { dao.checkTransaction(movieId)}
}