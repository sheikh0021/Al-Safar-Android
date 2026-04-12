package com.application.al_safar.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val access: String,
    val refresh: String,
    val user: UserDto
)

data class UserDto(
    val id: Int,
    val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("profile_picture") val profileImageUrl: String?,
    val role: String
)
