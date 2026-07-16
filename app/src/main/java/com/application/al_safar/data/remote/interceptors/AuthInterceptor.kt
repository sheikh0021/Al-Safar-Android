package com.application.al_safar.data.remote.interceptors

import com.application.al_safar.data.local.preferences.PreferencesManager
import com.application.al_safar.core.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
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
            if (refreshToken.isNullOrBlank()){
                response.close()
                runBlocking { preferencesManager.clearAll() }
                return response
            }

        val refreshRequest = okhttp3.Request.Builder()
            .url("${ApiConfig.BASE_URL}auth/refresh/")
            .post(
                """{"refresh": "$refreshToken"}"""
                    .toRequestBody("application/json".toMediaType())
            )
            .build()

        val refreshResponse = try {
            chain.proceed(refreshRequest)
        } catch (_: Exception){
            response.close()
            runBlocking { preferencesManager.clearAll() }
            return response
        }

        if (!refreshResponse.isSuccessful){
            refreshResponse.close()
            response.close()
            runBlocking { preferencesManager.clearAll() }
            return response
        }

        val bodyString = refreshResponse.body?.string().orEmpty()
        refreshResponse.close()

        val json = try {
            JSONObject(bodyString)
        } catch (_: Exception){
            response.close()
            runBlocking { preferencesManager.clearAll() }
            return response
        }

        val newAccess = json.optString("access").orEmpty()
        val newRefresh = json.optString("refresh").orEmpty()

        if (newAccess.isBlank()){
            response.close()
            runBlocking { preferencesManager.clearAll() }
            return response
        }

        runBlocking {
            preferencesManager.saveAccessToken(newAccess)
            if (newRefresh.isNotBlank()){
                preferencesManager.saveRefreshToken(newRefresh)
            }
        }
        response.close()
        val retried = request.newBuilder()
            .header("Authorization", "Bearer $newAccess")
            .build()
        return chain.proceed(retried)
    }
}