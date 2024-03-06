package com.fienna.movieapp.core.data.local.datasource

import com.fienna.movieapp.core.data.local.database.MovieDao
import com.fienna.movieapp.core.data.local.entity.CartEntity
import com.fienna.movieapp.core.data.local.entity.WishlistEntity
import com.fienna.movieapp.core.data.local.preferences.SharedPref
import com.fienna.movieapp.core.utils.safeDataCall
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val sharedPref: SharedPref,
    private val dao: MovieDao
) {
    fun getTokenValue(userId:String):Int = sharedPref.getTokenValue(userId)
    fun putTokenValue(userId:String, value:Int){sharedPref.putTokenValue(userId, value)}
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

    fun getCountWishlistFromDashboard():Int = sharedPref.getCountWishlistFromDashboard()
    fun putCountWishlistFromDashboard(value:Int){ sharedPref.putCountWishlistFromDashboard(value) }

    /*room database code*/
    fun fetchCart(userId:String): Flow<List<CartEntity>> = dao.retrieveAllCart(userId)
    fun fetchCheckedCart(userId: String) : Flow<List<CartEntity>> = dao.retrieveCheckedCart(userId)
    suspend fun deleteAllCart() = dao.deleteAllCart()
    suspend fun insertCart(cartEntity: CartEntity) { dao.insertCart(cartEntity) }
    suspend fun deleteCart(cartEntity: CartEntity){dao.deleteCart(cartEntity)}
    suspend fun checkAdd(movieId: Int, userId: String) : Int = safeDataCall { dao.checkAdd(movieId, userId) }
    suspend fun updateCheckCart(cartId:Int, value: Boolean, userId: String){dao.updateCheckCart(cartId, value, userId)}
    suspend fun updateTotalPriceChecked(userId: String):Int = safeDataCall { dao.updateTotalPriceChecked(userId) }
    fun fetchMovieCart(movieId: Int, userId: String): CartEntity = dao.retrieveMovieCart(movieId, userId)


    fun fetchWishlist(userId:String): Flow<List<WishlistEntity>> = dao.retrieveAllWishlist(userId)
    fun fetchMovieWishlist(movieId: Int, userId: String):WishlistEntity = dao.retrieveMovieWishlist(movieId, userId)
    suspend fun deleteAllWishlist() = dao.deleteAllWishlist()
    suspend fun insertWishlist(wishlistEntity: WishlistEntity) { dao.insertWishlist(wishlistEntity) }
    suspend fun deleteWishlist(wishlistEntity: WishlistEntity){dao.deleteWishlist(wishlistEntity)}
    suspend fun checkFavorite(movieId: Int, userId: String) : Int = safeDataCall { dao.checkFavorite(movieId, userId) }
    suspend fun countWishlist(userId:String) : Int = safeDataCall { dao.countWishlist(userId) }
}