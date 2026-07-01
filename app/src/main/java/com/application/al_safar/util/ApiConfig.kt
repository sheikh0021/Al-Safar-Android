package com.application.al_safar.util

import com.application.al_safar.BuildConfig

/**
 * Base URL for the Django backend API.
 * Set per flavor in app/build.gradle.kts:
 * - local      → http://10.0.2.2:8000/api/ (emulator + Django on your Mac)
 * - production → https://al-safar-backend.onrender.com/api/
 *
 * Switch: Android Studio → Build Variants → localDebug / productionDebug
 */
object ApiConfig {
    const val BASE_URL = BuildConfig.API_BASE_URL
}
