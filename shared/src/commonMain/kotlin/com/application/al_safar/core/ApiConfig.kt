package com.application.al_safar.core

/**
 * API base URL for shared networking code.
 *
 * Why no expect/actual here:
 * - Shared cannot read the app module's BuildConfig (circular dependency).
 * - The new Android KMP library plugin has no product-flavor BuildConfig.
 * - App flavors still own the URL; they call [initialize] at startup.
 */
object ApiConfig {
    private const val DEFAULT_BASE_URL = "https://al-safar-backend.onrender.com/api/"

    @Volatile
    private var baseUrl: String = DEFAULT_BASE_URL

    val BASE_URL: String
        get() = baseUrl

    fun initialize(url: String) {
        require(url.isNotBlank()) { "ApiConfig URL must not be blank" }
        baseUrl = if (url.endsWith("/")) url else "$url/"
    }
}
