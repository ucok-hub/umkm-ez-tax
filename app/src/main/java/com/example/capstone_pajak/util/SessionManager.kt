package com.example.capstone_pajak.util

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.capstone_pajak.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SessionManager {
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun logout(context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            // Clear DataStore
            DataStoreHelper.clearData(context)
            
            // Sign out from Firebase
            FirebaseAuthHelper().signOut()
            
            // Update logged in status
            _isLoggedIn.value = false
            
            // Clear activity stack and start LoginActivity
            val intent = Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }

    fun checkSession(context: Context) {
        val firebaseAuthHelper = FirebaseAuthHelper()
        if (!firebaseAuthHelper.isSessionValid()) {
            logout(context)
        }
    }
}
