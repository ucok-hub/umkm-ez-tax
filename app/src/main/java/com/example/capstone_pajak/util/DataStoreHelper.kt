package com.example.capstone_pajak.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension untuk DataStore
private val Context.dataStore by preferencesDataStore(name = "user_preferences")

object DataStoreHelper {
    private val IS_LOGGED_IN_KEY = booleanPreferencesKey("IS_LOGGED_IN")
    private val EMAIL_KEY = stringPreferencesKey("EMAIL")
    private val TOKEN_KEY = stringPreferencesKey("TOKEN")

    // Simpan status login
    suspend fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    // Ambil status login
    fun getLoginStatus(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN_KEY] ?: false
        }
    }

    // Simpan email pengguna
    suspend fun saveEmail(context: Context, email: String) {
        context.dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    // Ambil email pengguna
    fun getEmail(context: Context): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[EMAIL_KEY]
        }
    }

    // Simpan token login (untuk API login)
    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    // Ambil token login
    fun getToken(context: Context): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    // Hapus semua data (untuk logout)
    suspend fun clearData(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
