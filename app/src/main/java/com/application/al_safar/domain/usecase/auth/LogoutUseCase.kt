package com.application.al_safar.domain.usecase.auth

import com.application.al_safar.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
