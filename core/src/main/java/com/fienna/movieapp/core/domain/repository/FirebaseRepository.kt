package com.fienna.movieapp.core.domain.repository

import android.os.Bundle
import com.fienna.movieapp.core.domain.model.DataMovieTransaction
import com.fienna.movieapp.core.domain.model.DataToken
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface FirebaseRepository {
    fun signUp(email: String, password: String): Flow<Boolean>
    fun signIn(email: String, password: String): Flow<Boolean>
    fun getCurrentUser(): FirebaseUser?
    fun updateProfile(userProfileChangeRequest: UserProfileChangeRequest): Flow<Boolean>
    fun logScreenView(screenName: String)
    fun logEvent(eventName: String, bundle: Bundle)
    fun getConfigStatusToken(): Flow<Boolean>
    fun getConfigToken(): Flow<String>
    fun getConfigStatusPayment(): Flow<Boolean>
    fun getConfigPayment(): Flow<String>
    suspend fun sendDataToDatabase(dataToken: DataToken, userId: String): Flow<Boolean>
    suspend fun sendMovieToDatabase(
        dataMovieTransaction: DataMovieTransaction,
        userId: String,
        movieId: String
    ): Flow<Boolean>

    suspend fun getTokenFromFirebase(userId: String): Flow<Int>
    suspend fun getMovieTokenFromFirebase(userId: String): Flow<Int>

    suspend fun getMovieFromFirebase(userId: String, movieId: String): Flow<DataMovieTransaction?>
    suspend fun getAllMovieFromFirebase(userId: String):Flow<List<DataMovieTransaction>>

}

class FirebaseRepositoryImpl(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val auth: FirebaseAuth,
    private val remoteConfig: FirebaseRemoteConfig,
    private val firebaseDatabase: DatabaseReference
) : FirebaseRepository {
    override fun signUp(email: String, password: String): Flow<Boolean> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                trySend(task.isSuccessful)
                if (task.isSuccessful) {
                    println("MASUK : createUserWithEmail:success")
                } else {
                    println("MASUK : createUserWithEmail:failure ${task.exception}")
                }
            }
        awaitClose()
    }

    override fun signIn(email: String, password: String): Flow<Boolean> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                trySend(task.isSuccessful)
                if (task.isSuccessful) {
                    println("MASUK : signInWithEmail:success")
                } else {
                    println("MASUK : signInWithEmail:failure ${task.exception}")
                }
            }.addOnFailureListener {
                println("MASUK : signInWithEmail:failure ${it.message}")
            }

        awaitClose()
    }

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override fun updateProfile(userProfileChangeRequest: UserProfileChangeRequest): Flow<Boolean> =
        callbackFlow {
            getCurrentUser()?.updateProfile(userProfileChangeRequest)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(task.isSuccessful)
                        println("MASUK : User profile updated.")
                    } else {
                        println("MASUK : User profile error.")
                    }
                }
            awaitClose()
        }

    override fun logScreenView(screenName: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        firebaseAnalytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            bundle
        )
    }

    override fun logEvent(eventName: String, bundle: Bundle) {
        firebaseAnalytics.logEvent(
            eventName,
            bundle
        )
    }

    override fun getConfigStatusToken(): Flow<Boolean> = callbackFlow {
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains("token_list")) {
                    remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                        trySend(task.isSuccessful)
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                trySend(false)
            }
        })
        awaitClose()
    }

    override fun getConfigToken(): Flow<String> = callbackFlow {
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            trySend(remoteConfig.getString("token_list"))
        }
        awaitClose()
    }

    override fun getConfigStatusPayment(): Flow<Boolean> = callbackFlow {
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains("payment_list")) {
                    remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                        trySend(task.isSuccessful)
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                trySend(false)
            }
        })
        awaitClose()
    }

    override fun getConfigPayment(): Flow<String> = callbackFlow {
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            trySend(remoteConfig.getString("payment_list"))
        }
        awaitClose()
    }

    override suspend fun sendDataToDatabase(dataToken: DataToken, userId: String): Flow<Boolean> =
        callbackFlow {
            trySend(false)
            firebaseDatabase.database.reference.child("user_token").child(userId).push()
                .setValue(dataToken)
                .addOnCompleteListener { task ->
                    trySend(task.isSuccessful)
                }.addOnFailureListener { e ->
                    trySend(e.message?.isNotEmpty() ?: false)
                }
            awaitClose()
        }

    override suspend fun sendMovieToDatabase(
        dataMovieTransaction: DataMovieTransaction,
        userId: String,
        movieId: String
    ): Flow<Boolean> = callbackFlow {
        trySend(false)
        firebaseDatabase.database.reference.child("user_movie_transaction").child(movieId)
            .child(userId).push()
            .setValue(dataMovieTransaction)
            .addOnCompleteListener { task ->
                trySend(task.isSuccessful)
            }
            .addOnFailureListener { e ->
                trySend(e.message?.isNotEmpty() ?: false)
            }
        awaitClose()
    }

    override suspend fun getTokenFromFirebase(userId: String): Flow<Int> = callbackFlow {
        trySend(0)
        firebaseDatabase.database.reference.child("user_token").child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var totalToken = 0

                    for (keySnapshot in snapshot.children) {
                        for (dataToken in keySnapshot.children) {
                            val token = dataToken.child("token").getValue(String::class.java)
                            val tokenValue = token?.toIntOrNull() ?: 0
                            totalToken += tokenValue
                        }
                        trySend(totalToken)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(0)
                }
            })
        awaitClose()
    }

    override suspend fun getMovieTokenFromFirebase(userId: String): Flow<Int> = callbackFlow {
        trySend(0)
        firebaseDatabase.database.reference.child("user_movie_transaction").child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var totalPrice = 0

                    for (dataMovieTransaction in snapshot.children) {
                        val price =
                            dataMovieTransaction.child("priceMovie").getValue(Int::class.java)
                        if (price != null) {
                            totalPrice += price
                        }
                        trySend(totalPrice)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(0)
                }
            })
        awaitClose()
    }

    override suspend fun getMovieFromFirebase(
        userId: String,
        movieId: String
    ): Flow<DataMovieTransaction?> = callbackFlow {
        val query = firebaseDatabase.database.reference
            .child("user_movie_transaction")
            .child(movieId)
            .child(userId)

        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (id in snapshot.children) {
                    for (keySnapshot in id.children){
                        val dataMovie = keySnapshot.getValue(DataMovieTransaction::class.java)
                        trySend(dataMovie)
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        query.addValueEventListener(eventListener)

        awaitClose()
    }

    override suspend fun getAllMovieFromFirebase(userId: String): Flow<List<DataMovieTransaction>> = callbackFlow{
        firebaseDatabase.database.reference.child("user_movie_transaction").child(userId).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val movies = mutableListOf<DataMovieTransaction>()
                for (movieSnapshot in snapshot.children){
                    for (id in movieSnapshot.children){
                        for(key in id.children){
                            println("masuk movie key $key")
                            val movie = key.getValue(DataMovieTransaction::class.java)
                            movie?.let { movies.add(it) }
                            println("masuk nilai movie $movie")
                        }
                    }
                }
                trySend(movies)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
                println("error realtime  $error")
            }

        })
        awaitClose()
    }


}