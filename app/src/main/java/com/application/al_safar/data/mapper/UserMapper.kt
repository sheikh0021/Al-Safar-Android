package com.application.al_safar.data.mapper

import com.application.al_safar.data.local.entities.UserEntity
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

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}