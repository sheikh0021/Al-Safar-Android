package com.application.al_safar.domain.model


data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?,
    val profileImageUrl: String?
)
