package com.fienna.movieapp.core.domain.repository

import com.fienna.movieapp.core.data.local.datasource.LocalDataSource
import com.fienna.movieapp.core.data.local.entity.CartEntity
import com.fienna.movieapp.core.data.local.entity.WishlistEntity
import com.fienna.movieapp.core.utils.safeDataCall
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun fetchCart(userId:String): Flow<List<CartEntity>>
    suspend fun fetchCheckedCart(userId: String) : Flow<List<CartEntity>>
    suspend fun deleteAllCart()
    suspend fun insertCart(cartEntity: CartEntity)
    suspend fun deleteCart(cartEntity: CartEntity)
    suspend fun checkAdd(movieId:Int, userId: String) : Int
    suspend fun updateCheckCart(cartId: Int, value:Boolean, userId: String)
    suspend fun updateTotalPriceChecked(userId: String): Int = 0
    suspend fun fetchMovieCart(movieId: Int, userId: String): CartEntity
    suspend fun fetchWishlist(userId:String): Flow<List<WishlistEntity>>
    suspend fun fetchMovieWishlist(movieId: Int,userId: String):WishlistEntity
    suspend fun deleteAllWishlist()
    suspend fun insertWishlist(wishlistEntity: WishlistEntity)
    suspend fun deleteWishlist(wishlistEntity: WishlistEntity)
    suspend fun checkFavorite(movieId:Int, userId: String) : Int
    suspend fun countWishlist(userId:String) : Int
}

class RoomRepositoryImpl(
    private val local:LocalDataSource
):RoomRepository{
    override suspend fun fetchCart(userId: String): Flow<List<CartEntity>> = safeDataCall {
        local.fetchCart(userId)
    }

    override suspend fun fetchCheckedCart(userId: String): Flow<List<CartEntity>> = safeDataCall{
        local.fetchCheckedCart(userId)
    }

    override suspend fun deleteAllCart(){
        local.deleteAllCart()
    }

    override suspend fun insertCart(cartEntity: CartEntity){
        local.insertCart(cartEntity)
    }

    override suspend fun deleteCart(cartEntity: CartEntity){
        local.deleteCart(cartEntity)
    }

    override suspend fun checkAdd(movieId: Int, userId: String): Int = safeDataCall{
        local.checkAdd(movieId, userId)
    }

    override suspend fun updateCheckCart(cartId: Int, value: Boolean, userId: String) {
        local.updateCheckCart(cartId, value, userId)
    }

    override suspend fun updateTotalPriceChecked(userId: String): Int = safeDataCall {
        local.updateTotalPriceChecked(userId)
    }

    override suspend fun fetchMovieCart(movieId: Int, userId: String): CartEntity = safeDataCall {
        local.fetchMovieCart(movieId, userId)
    }

    override suspend fun fetchWishlist(userId: String): Flow<List<WishlistEntity>> = safeDataCall{
        local.fetchWishlist(userId)
    }

    override suspend fun fetchMovieWishlist(movieId: Int, userId: String): WishlistEntity = safeDataCall{
        local.fetchMovieWishlist(movieId, userId)
    }

    override suspend fun deleteAllWishlist() {
        local.deleteAllWishlist()
    }

    override suspend fun insertWishlist(wishlistEntity: WishlistEntity)  {
        local.insertWishlist(wishlistEntity)
    }

    override suspend fun deleteWishlist(wishlistEntity: WishlistEntity){
        local.deleteWishlist(wishlistEntity)
    }

    override suspend fun checkFavorite(movieId: Int, userId: String): Int = safeDataCall{
        local.checkFavorite(movieId, userId)
    }

    override suspend fun countWishlist(userId: String): Int = local.countWishlist(userId)



}