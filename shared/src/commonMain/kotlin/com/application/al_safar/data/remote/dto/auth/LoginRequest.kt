package com.application.al_safar.data.remote.dto.auth

import kotlinx.serialization.Serializable

/**
 * Body sent to POST /api/auth/login/
 * Backend expects "username" (even if value is an email).
 */
@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)
