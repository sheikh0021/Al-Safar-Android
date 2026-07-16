package com.application.al_safar.data.remote

import io.ktor.client.engine.okhttp.OkHttp

/**
 * Android entry point for shared networking.
 * Keeps OkHttp / HttpClient out of app Hilt modules (kapt + KMP friendly).
 */
object AndroidNetwork {
    fun createAuthRemoteDataSource(): AuthRemoteDataSource {
        return AuthRemoteDataSource(HttpClientFactory.create(OkHttp))
    }
}
