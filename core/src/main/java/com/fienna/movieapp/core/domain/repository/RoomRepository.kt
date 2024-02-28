package com.fienna.movieapp.core.domain.repository

import com.fienna.movieapp.core.data.local.datasource.LocalDataSource
import com.fienna.movieapp.core.data.local.entity.CartEntity
import com.fienna.movieapp.core.data.local.entity.WishlistEntity
import com.fienna.movieapp.core.utils.safeDataCall
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun fetchCart(userId:String): Flow<List<CartEntity>>
    suspend fun deleteAllCart()
    suspend fun insertCart(cartEntity: CartEntity)
    suspend fun deleteCart(cartEntity: CartEntity)
    suspend fun checkAdd(movieId:Int) : Int

    suspend fun fetchWishlist(userId:String): Flow<List<WishlistEntity>>
    suspend fun deleteAllWishlist()
    suspend fun insertWishlist(wishlistEntity: WishlistEntity)
    suspend fun deleteWishlist(wishlistEntity: WishlistEntity)
    suspend fun checkFavorite(movieId:Int) : Int
}

class RoomRepositoryImpl(
    private val local:LocalDataSource
):RoomRepository{
    override suspend fun fetchCart(userId: String): Flow<List<CartEntity>> = safeDataCall {
        local.fetchCart(userId)
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

    override suspend fun checkAdd(movieId: Int): Int = safeDataCall{
        local.checkAdd(movieId)
    }

    override suspend fun fetchWishlist(userId: String): Flow<List<WishlistEntity>> = safeDataCall{
        local.fetchWishlist(userId)
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

    override suspend fun checkFavorite(movieId: Int): Int = safeDataCall{
        local.checkFavorite(movieId)
    }


}