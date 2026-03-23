package com.application.al_safar.domain.usecase.auth

import com.application.al_safar.domain.model.User
import com.application.al_safar.domain.repository.AuthRepository
import javax.inject.Inject

class SignupUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String?
    ): Result<User> {
        return repository.signup(email, password, firstName, lastName, phoneNumber)
    }
}
