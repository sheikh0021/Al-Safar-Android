package com.application.al_safar.data.remote.dto.auth

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String? = null
)
