package com.application.al_safar.domain.usecase.auth

import com.application.al_safar.domain.repository.AuthRepository


class LogoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}