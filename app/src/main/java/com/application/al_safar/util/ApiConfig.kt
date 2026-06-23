package com.application.al_safar.util

import com.application.al_safar.BuildConfig

/**
 * Base URL for the Django backend API.
 * Configure via buildConfigField "API_BASE_URL" in app/build.gradle.kts:
 * - Emulator: "http://10.0.2.2:8000/api/"
 * - Physical device (same Wi‑Fi): "http://YOUR_PC_IP:8000/api/"
 * - Production: "https://your-domain.com/api/"
 */
object ApiConfig {
    const val BASE_URL = BuildConfig.API_BASE_URL
}
