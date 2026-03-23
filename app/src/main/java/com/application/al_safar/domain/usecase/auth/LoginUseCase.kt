package com.application.al_safar.domain.usecase.auth

import com.application.al_safar.domain.model.User
import com.application.al_safar.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.login(email, password)
    }
}