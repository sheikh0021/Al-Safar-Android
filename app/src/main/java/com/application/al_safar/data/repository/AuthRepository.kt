package com.application.al_safar.data.repository

import com.application.al_safar.data.local.dao.UserDao
import com.application.al_safar.data.local.preferences.PreferencesManager
import com.application.al_safar.data.mapper.toDomain
import com.application.al_safar.data.mapper.toEntity
import com.application.al_safar.data.remote.api.AuthApi
import com.application.al_safar.data.remote.dto.auth.LoginRequest
import com.application.al_safar.data.remote.dto.auth.SignupRequest
import com.application.al_safar.domain.model.User
import com.application.al_safar.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val userDao: UserDao,
    private val preferencesManager: PreferencesManager
): AuthRepository {
    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = authApi.login(LoginRequest(username = email, password = password))
            if ( response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                preferencesManager.saveAccessToken(loginResponse.access)
                preferencesManager.saveRefreshToken(loginResponse.refresh)
                preferencesManager.saveUserId(loginResponse.user.id.toString())
                preferencesManager.saveUserRole(loginResponse.user.role)
                preferencesManager.setLoggedIn(true)

                val user = loginResponse.user.toDomain()
                userDao.insertUser(user.toEntity())

                Result.success(user)
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun signup(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String?,
        role: String
    ): Result<User> {
        return try {
            val response = authApi.signup(
                SignupRequest(
                    username = email,
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                    role = role
                )
            )
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                preferencesManager.saveAccessToken(loginResponse.access)
                preferencesManager.saveRefreshToken(loginResponse.refresh)
                preferencesManager.saveUserId(loginResponse.user.id.toString())
                preferencesManager.saveUserRole(loginResponse.user.role)
                preferencesManager.setLoggedIn(true)

                val user = loginResponse.user.toDomain()
                userDao.insertUser(user.toEntity())
                Result.success(user)
            } else {
                Result.failure(Exception("Signup failed: ${response.message()}"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        try {
            authApi.logout()
        }catch (e: Exception) {
            //continue with local logout
        } finally {
            preferencesManager.clearAll()
            userDao.clearAll()
        }
    }

    override fun getCurrentUser(): Flow<User?> {
        return preferencesManager.userId.flatMapLatest { userId ->
            if (userId != null) {
                userDao.getUserById(userId).map { it?.toDomain() }
            } else {
                flowOf(null)
            }
        }
    }
}