package com.application.al_safar.data.mapper

import com.application.al_safar.data.local.entities.UserEntity
import com.application.al_safar.domain.model.User

/**
 * Room mappers for User.
 * File must NOT be named UserMapper.kt — shared module already has
 * com.application.al_safar.data.mapper.UserMapper.kt, and both would compile
 * to the same JVM class UserMapperKt (runtime NoSuchMethodError for toEntity).
 */
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
