package com.application.al_safar.util

/**
 * Base URL for the Django backend API.
 * - Emulator: use "http://10.0.2.2:8000/api/"
 * - Physical device (same Wi‑Fi): use "http://YOUR_PC_IP:8000/api/" (e.g. "http://192.168.1.100:8000/api/")
 * - Production: use "https://your-domain.com/api/"
 */
object ApiConfig {
    const val BASE_URL = "http://10.0.2.2:8000/api/"
}
