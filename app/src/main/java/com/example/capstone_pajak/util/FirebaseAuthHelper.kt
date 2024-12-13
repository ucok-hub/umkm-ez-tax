package com.example.capstone_pajak.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class FirebaseAuthHelper {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun registerUser(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isSessionValid(): Boolean {
        val user = auth.currentUser
        if (user == null) return false

        val lastSignInTime = user.metadata?.lastSignInTimestamp ?: return false
        val currentTime = System.currentTimeMillis()
        val thirtyDaysInMillis = TimeUnit.DAYS.toMillis(30)

        return (currentTime - lastSignInTime) < thirtyDaysInMillis
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}
