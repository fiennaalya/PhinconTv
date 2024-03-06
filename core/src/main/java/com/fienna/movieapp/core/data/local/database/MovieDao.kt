package com.fienna.movieapp.core.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fienna.movieapp.core.data.local.entity.CartEntity
import com.fienna.movieapp.core.data.local.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartEntity)
    @Query("SELECT * FROM table_cart WHERE user_id = :userId")
    fun retrieveAllCart(userId: String): Flow<List<CartEntity>>
    @Query("DELETE FROM table_cart")
    suspend fun deleteAllCart()
    @Delete
    suspend fun deleteCart(cart:CartEntity)
    @Query("SELECT EXISTS (SELECT * FROM table_cart WHERE movie_id = :movieId AND user_id = :userId)")
    suspend fun checkAdd(movieId: Int, userId: String): Int
    @Query("UPDATE table_cart SET isChecked = :value WHERE cart_id= :cartId AND user_id =:userId")
    suspend fun updateCheckCart(cartId: Int, value:Boolean, userId: String)
    @Query("SELECT COALESCE(SUM(popularity), 0) FROM table_cart WHERE isChecked = 1 AND user_id = :userId")
    suspend fun updateTotalPriceChecked(userId: String): Int
    @Query("SELECT * FROM table_cart WHERE movie_id = :movieId AND user_id = :userId")
    fun retrieveMovieCart(movieId: Int, userId: String): CartEntity
    @Query("SELECT * FROM table_cart WHERE isChecked = 1 AND user_id = :userId")
    fun retrieveCheckedCart(userId: String) : Flow<List<CartEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlist(wishlist: WishlistEntity)
    @Query("SELECT * FROM table_wishlist WHERE user_id = :userId")
    fun retrieveAllWishlist(userId: String): Flow<List<WishlistEntity>>
    @Query("SELECT * FROM table_wishlist WHERE movie_id = :movieId AND user_id = :userId")
    fun retrieveMovieWishlist(movieId: Int, userId: String): WishlistEntity

    @Query("DELETE FROM table_wishlist")
    suspend fun deleteAllWishlist()
    @Delete
    suspend fun deleteWishlist(cart:WishlistEntity)
    @Query("SELECT EXISTS  (SELECT * FROM table_wishlist WHERE movie_id = :movieId AND user_id = :userId)")
    suspend fun checkFavorite(movieId: Int, userId: String): Int
    @Query("SELECT count(*) FROM table_wishlist WHERE user_id = :userId")
    suspend fun countWishlist(userId: String):Int

}