package com.application.al_safar.data.repository

import com.application.al_safar.data.local.dao.UserDao
import com.application.al_safar.data.local.preferences.PreferencesManager
import com.application.al_safar.data.mapper.toDomain
import com.application.al_safar.data.mapper.toEntity
import com.application.al_safar.data.remote.AuthRemoteDataSource
import com.application.al_safar.data.remote.api.AuthApi
import com.application.al_safar.data.remote.dto.auth.LogoutRequest
import com.application.al_safar.domain.model.User
import com.application.al_safar.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authApi: AuthApi,
    private val userDao: UserDao,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return authRemoteDataSource.login(email, password).mapCatching { (loginResponse, user) ->
            preferencesManager.saveAccessToken(loginResponse.access)
            preferencesManager.saveRefreshToken(loginResponse.refresh)
            preferencesManager.saveUserId(loginResponse.user.id.toString())
            preferencesManager.saveUserRole(loginResponse.user.role)
            preferencesManager.saveUserName(
                "${loginResponse.user.firstName} ${loginResponse.user.lastName}".trim()
            )
            preferencesManager.setLoggedIn(true)
            userDao.insertUser(user.toEntity())
            user
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
        return authRemoteDataSource.signup(
            email = email,
            password = password,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            role = role
        ).mapCatching { (loginResponse, user) ->
            preferencesManager.saveAccessToken(loginResponse.access)
            preferencesManager.saveRefreshToken(loginResponse.refresh)
            preferencesManager.saveUserId(loginResponse.user.id.toString())
            preferencesManager.saveUserRole(loginResponse.user.role)
            preferencesManager.saveUserName(
                "${loginResponse.user.firstName} ${loginResponse.user.lastName}".trim()
            )
            preferencesManager.setLoggedIn(true)
            userDao.insertUser(user.toEntity())
            user
        }
    }

    override suspend fun logout() {
        try {
            val refresh = preferencesManager.refreshToken.first()
            if (!refresh.isNullOrBlank()) {
                authApi.logout(LogoutRequest(refresh = refresh))
            }
        } catch (_: Exception) {
            // Network/API failure must never block local logout
        } finally {
            preferencesManager.clearAll()
            userDao.clearAll()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
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
