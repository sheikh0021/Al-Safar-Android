package com.application.al_safar.data.remote.api

import com.application.al_safar.data.remote.dto.auth.ForgotPasswordRequest
import com.application.al_safar.data.remote.dto.auth.LoginRequest
import com.application.al_safar.data.remote.dto.auth.LoginResponse
import com.application.al_safar.data.remote.dto.auth.SignupRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/signup/")
    suspend fun signup(@Body request: SignupRequest): Response<LoginResponse>

    @POST("auth/forget-password/")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<Unit>

    @POST("auth/logout/")
    suspend fun logout(): Response<Unit>
}