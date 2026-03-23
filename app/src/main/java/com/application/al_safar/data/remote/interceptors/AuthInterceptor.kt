package com.application.al_safar.data.remote.interceptors

import com.application.al_safar.data.local.preferences.PreferencesManager
import com.application.al_safar.util.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response


class AuthInterceptor(
    private val preferencesManager: PreferencesManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.encodedPath.contains("auth/login") ||
            request.url.encodedPath.contains("auth/signup") ||
            request.url.encodedPath.contains("auth/forget-password")) {
            return chain.proceed(request)
        }

        val token = runBlocking {
            preferencesManager.accessToken.first()
        }
        val authenticatedRequest = token?.let {
            request.newBuilder()
                .header("Authorization", "Bearer $it")
                .build()
        } ?: request

        val response = chain.proceed(authenticatedRequest)

        if (response.code == 401) {
val refreshToken = runBlocking { preferencesManager.refreshToken.first() }
            //cannot use Retrofit here
            if (refreshToken != null) {
                val refreshRequest = okhttp3.Request.Builder()
                    .url("${ApiConfig.BASE_URL}auth/refresh/")
                    .post(
                        okhttp3.RequestBody.create(
                            "application/json".toMediaTypeOrNull(),
                            """{"refresh": "$refreshToken"}"""
                        )
                    )
                    .build()
                val refreshResponse = chain.proceed(refreshRequest)
                if (refreshResponse.isSuccessful) {
                    val newAccessToken = /* parse JSON body for "access" field */ ""
                    runBlocking { preferencesManager.saveAccessToken(newAccessToken) }
                    val retried = request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                    return chain.proceed(retried)
                } else {
                    runBlocking { preferencesManager.clearAll() }
                }
            }
        }
        return response
    }
}