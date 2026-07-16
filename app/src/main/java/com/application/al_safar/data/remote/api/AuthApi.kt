package com.application.al_safar.data.remote.api

import com.application.al_safar.data.remote.dto.auth.ForgotPasswordRequest
import com.application.al_safar.data.remote.dto.auth.LogoutRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit auth endpoints still used for logout / forgot-password.
 * Login + signup go through shared [com.application.al_safar.data.remote.AuthRemoteDataSource] (Ktor).
 */
interface AuthApi {
    @POST("auth/forget-password/")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<Unit>

    @POST("auth/logout/")
    suspend fun logout(@Body request: LogoutRequest): Response<Unit>
}
