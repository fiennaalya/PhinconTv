package com.fienna.movieapp.core.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface FirebaseRepository{
    fun signUp(email:String, password:String):Flow<Boolean>
    fun signIn(email:String, password:String):Flow<Boolean>
    fun getCurrentUser():FirebaseUser?
    fun updateProfile(userProfileChangeRequest: UserProfileChangeRequest):Flow<Boolean>
}
class FirebaseRepositoryImpl (private val auth: FirebaseAuth):FirebaseRepository{
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
            }.addOnFailureListener {}

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

}