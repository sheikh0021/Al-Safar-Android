package com.application.al_safar.domain.model


data class LocalGuide(
    val id: Int,
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber : String?,
    val bio: String?,
    val city: String,
    val languagesList: List<String>,
    val isAvailable: Boolean,
    val rating: String,
    val profilePictureUrl: String?
)