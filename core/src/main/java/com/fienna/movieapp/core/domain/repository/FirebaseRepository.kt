package com.fienna.movieapp.core.domain.repository

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface FirebaseRepository{
    fun signUp(email:String, password:String):Flow<Boolean>
    fun signIn(email:String, password:String):Flow<Boolean>
    fun getCurrentUser():FirebaseUser?
    fun updateProfile(userProfileChangeRequest: UserProfileChangeRequest):Flow<Boolean>
    fun logScreenView(screenName:String)
    fun logEvent(eventName:String, bundle: Bundle)
    fun getConfigStatusToken():Flow<Boolean>
    fun getConfigToken():Flow<String>
    fun getConfigStatusPayment():Flow<Boolean>
    fun getConfigPayment():Flow<String>
}
class FirebaseRepositoryImpl (
    private val firebaseAnalytics: FirebaseAnalytics,
    private val auth: FirebaseAuth,
    private val remoteConfig: FirebaseRemoteConfig
):FirebaseRepository{
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

    override fun signIn(email: String, password: String): Flow<Boolean> = callbackFlow{
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
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

    override fun updateProfile(userProfileChangeRequest: UserProfileChangeRequest): Flow<Boolean> = callbackFlow{
        getCurrentUser()?.updateProfile(userProfileChangeRequest)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    trySend(task.isSuccessful)
                    println("MASUK : User profile updated.")
                } else{
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
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener{
            override fun onUpdate(configUpdate: ConfigUpdate) {
                println("MASUK CONFIGSTATUSTOKEN : update ${configUpdate.updatedKeys}")
                if (configUpdate.updatedKeys.contains("token_list")){
                    remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                        trySend(task.isSuccessful)
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                println("MASUK CONFIGSTATUSTOKEN : Dynamic Remote Config Error: $error")
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

    override fun getConfigStatusPayment(): Flow<Boolean> = callbackFlow{
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener{
            override fun onUpdate(configUpdate: ConfigUpdate) {
                println("MASUK CONFIGSTATUS PAYMENT: update ${configUpdate.updatedKeys}")
                if (configUpdate.updatedKeys.contains("payment_list")) {
                    remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                        trySend(task.isSuccessful)
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                println("MASUK CONFIGSTATUS PAYMENT : Dynamic Remote Config Error: $error")
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
}