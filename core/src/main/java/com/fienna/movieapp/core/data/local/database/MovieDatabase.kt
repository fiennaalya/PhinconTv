package com.fienna.movieapp.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fienna.movieapp.core.data.local.entity.CartEntity
import com.fienna.movieapp.core.data.local.entity.TransactionEntity
import com.fienna.movieapp.core.data.local.entity.WishlistEntity

@Database(entities = [CartEntity::class, WishlistEntity::class, TransactionEntity::class], version = 3)
abstract class MovieDatabase:RoomDatabase() {
    abstract fun movieDao(): MovieDao
}