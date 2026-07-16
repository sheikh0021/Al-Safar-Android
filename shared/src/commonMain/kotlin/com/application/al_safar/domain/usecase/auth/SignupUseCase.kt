package com.application.al_safar.domain.usecase.auth

import com.application.al_safar.domain.model.User
import com.application.al_safar.domain.repository.AuthRepository


class SignupUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String?,
        role: String
    ): Result<User> {
        return repository.signup(
            email = email,
            password = password,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            role = role
        )
    }
}