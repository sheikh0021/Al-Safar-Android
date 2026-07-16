package com.application.al_safar.core

import kotlinx.coroutines.flow.Flow


interface SessionStorage {
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
    val userId: Flow<String?>
    val userRole: Flow<String>
    val userName: Flow<String>
    val isLoggedIn: Flow<Boolean>

    suspend fun saveAccessToken(token: String)
    suspend fun saveRefreshToken(token: String)
    suspend fun saveUserId(id: String)
    suspend fun saveUserRole(role: String)
    suspend fun saveUserName(name: String)
    suspend fun setLoggedIn(value: Boolean)
    suspend fun clearAll()
}