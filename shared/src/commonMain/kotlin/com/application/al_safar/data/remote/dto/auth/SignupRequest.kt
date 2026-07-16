package com.application.al_safar.data.remote.dto.auth

import kotlinx.serialization.Serializable

/**
 * Body sent to POST /api/auth/signup/
 * Backend reads camelCase keys (firstName, lastName, phoneNumber).
 */
@Serializable
data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String? = null,
    val role: String = "traveler"
)
