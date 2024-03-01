package com.fienna.movieapp.core.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fienna.movieapp.core.data.local.entity.CartEntity
import com.fienna.movieapp.core.data.local.entity.TransactionEntity
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
    @Query("SELECT count(*) FROM table_cart WHERE movie_id = :movieId")
    suspend fun checkAdd(movieId: Int): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlist(wishlist: WishlistEntity)
    @Query("SELECT * FROM table_wishlist WHERE user_id = :userId")
    fun retrieveAllWishlist(userId: String): Flow<List<WishlistEntity>>
    @Query("DELETE FROM table_wishlist")
    suspend fun deleteAllWishlist()
    @Delete
    suspend fun deleteWishlist(cart:WishlistEntity)
    @Query("SELECT count(*) FROM table_wishlist WHERE movie_id = :movieId")
    suspend fun checkFavorite(movieId: Int): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)
    @Query("SELECT * FROM table_transaction WHERE user_id = :userId")
    fun retrieveAllTransaction(userId: String): Flow<List<TransactionEntity>>
    @Query("SELECT count(*) FROM table_transaction WHERE movie_id = :movieId")
    suspend fun checkTransaction(movieId: Int): Int
    @Query("SELECT * FROM table_transaction WHERE movie_id = :movieId")
    fun retrieveTransactionsForMovie(movieId: Int): Flow<TransactionEntity>



}