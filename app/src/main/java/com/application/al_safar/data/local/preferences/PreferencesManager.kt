package com.application.al_safar.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "al_safar_prefs")

class PreferencesManager(private val context: Context) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        private val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")
        private val LANGUAGE_KEY = stringPreferencesKey("language")
    }

    val accessToken: Flow<String?> = context.dataStore.data.map { it[ACCESS_TOKEN_KEY] }
    val refreshToken: Flow<String?> = context.dataStore.data.map { it[REFRESH_TOKEN_KEY] }
    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID_KEY] }
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[IS_LOGGED_IN_KEY] ?: false }
    val fcmToken: Flow<String?> = context.dataStore.data.map { it[FCM_TOKEN_KEY] }
    val language: Flow<String> = context.dataStore.data.map { it[LANGUAGE_KEY] ?: "en" }

    suspend fun getAccessToken(): String? = accessToken.first()
    suspend fun isLoggedIn(): Boolean = isLoggedIn.first()
    suspend fun getLanguage(): String? = context.dataStore.data.first()[LANGUAGE_KEY]

    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { it[ACCESS_TOKEN_KEY] = token }
    }

    suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { it[REFRESH_TOKEN_KEY] = token }
    }

    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { it[USER_ID_KEY] = userId }
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { it[IS_LOGGED_IN_KEY] = isLoggedIn }
    }

    suspend fun saveFcmToken(token: String) {
        context.dataStore.edit { it[FCM_TOKEN_KEY] = token }
    }

    suspend fun saveLanguage(language: String) {
        context.dataStore.edit { it[LANGUAGE_KEY] = language }
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}