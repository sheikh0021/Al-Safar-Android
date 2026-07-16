package com.application.al_safar.data.remote

import com.application.al_safar.data.mapper.toDomain
import com.application.al_safar.data.remote.dto.auth.LoginRequest
import com.application.al_safar.data.remote.dto.auth.LoginResponse
import com.application.al_safar.data.remote.dto.auth.SignupRequest
import com.application.al_safar.domain.model.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

/**
 * Talks to Django auth endpoints using Ktor.
 * Replaces Retrofit AuthApi for login/signup in shared code.
 */
class AuthRemoteDataSource(
    private val client: HttpClient
) {
    suspend fun login(email: String, password: String): Result<Pair<LoginResponse, User>> {
        return runCatching {
            val response = client.post("auth/login/") {
                setBody(LoginRequest(username = email, password = password))
            }

            if (!response.status.isSuccess()) {
                error("Login failed: HTTP ${response.status.value}")
            }

            val body: LoginResponse = response.body()
            body to body.user.toDomain()
        }
    }

    suspend fun signup(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String?,
        role: String
    ): Result<Pair<LoginResponse, User>> {
        return runCatching {
            val response = client.post("auth/signup/") {
                setBody(
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
            }

            if (!response.status.isSuccess()) {
                error("Signup failed: HTTP ${response.status.value}")
            }

            val body: LoginResponse = response.body()
            body to body.user.toDomain()
        }
    }
}
