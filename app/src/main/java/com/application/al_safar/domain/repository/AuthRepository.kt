package com.application.al_safar.domain.repository

import com.application.al_safar.domain.model.User
import kotlinx.coroutines.flow.Flow


interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun signup(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String?
    ): Result<User>
    suspend fun logout()
    fun getCurrentUser(): Flow<User?>
}