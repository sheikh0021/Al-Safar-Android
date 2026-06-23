package com.application.al_safar.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.User
import com.application.al_safar.domain.usecase.auth.SignupUseCase
import com.application.al_safar.domain.usecase.notification.RegisterFcmTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase,
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignupUiState>(SignupUiState.Idle)
    val uiState: StateFlow<SignupUiState> = _uiState

    fun signup(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String?,
        role: String = "traveler"
    ) {
        viewModelScope.launch {
            _uiState.value = SignupUiState.Loading
            signupUseCase(email, password, firstName, lastName, phoneNumber, role)
                .onSuccess { user ->
                    _uiState.value = SignupUiState.Success(user)
                    launch { registerFcmTokenUseCase() }
                }
                .onFailure { _uiState.value = SignupUiState.Error(it.message ?: "Signup failed") }
        }
    }
}

sealed class SignupUiState {
    object Idle : SignupUiState()
    object Loading : SignupUiState()
    data class Success(val user: User) : SignupUiState()
    data class Error(val message: String) : SignupUiState()
}
