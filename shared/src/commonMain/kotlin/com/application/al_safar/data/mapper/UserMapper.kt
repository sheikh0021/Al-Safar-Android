package com.application.al_safar.data.mapper

import com.application.al_safar.data.remote.dto.auth.UserDto
import com.application.al_safar.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id.toString(),
        email = email,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl,
        role = role
    )
}
