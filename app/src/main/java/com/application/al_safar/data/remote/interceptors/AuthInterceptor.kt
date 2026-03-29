package com.application.al_safar.data.remote.interceptors

import com.application.al_safar.data.local.preferences.PreferencesManager
import com.application.al_safar.util.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import org.json.JSONObject

class AuthInterceptor(
    private val preferencesManager: PreferencesManager
) : Interceptor {

    private val skipPaths = listOf("auth/login", "auth/signup", "auth/forget-password", "auth/refresh")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (skipPaths.any { request.url.encodedPath.contains(it) }) {
            return chain.proceed(request)
        }

        val token = runBlocking { preferencesManager.accessToken.first() }

        val authenticatedRequest = token?.let {
            request.newBuilder().header("Authorization", "Bearer $it").build()
        } ?: request

        val response = chain.proceed(authenticatedRequest)

        if (response.code != 401) return response

        val refreshToken = runBlocking { preferencesManager.refreshToken.first() }
            ?: return response

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
            val bodyString = refreshResponse.body?.string()
            val newAccessToken = try {
                JSONObject(bodyString ?: "").getString("access")
            } catch (e: Exception) {
                null
            }

            if (!newAccessToken.isNullOrBlank()) {
                runBlocking { preferencesManager.saveAccessToken(newAccessToken) }
                val retried = request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
                return chain.proceed(retried)
            }
        }

        runBlocking { preferencesManager.clearAll() }
        return response
    }
}