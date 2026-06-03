package com.application.al_safar.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.User
import com.application.al_safar.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: User) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState : StateFlow<ProfileUiState> = _uiState

    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut : StateFlow<Boolean> = _isLoggingOut

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            authRepository.getCurrentUser()
                .catch { exception ->
                    _uiState.value = ProfileUiState.Error(
                        exception.localizedMessage ?: "Failed to load profile"
                    )
                }
                .collect { user ->
                    if (user != null) {
                        _uiState.value = ProfileUiState.Success(user)
                    } else {
                        _uiState.value = ProfileUiState.Error("No user session found")
                    }
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoggingOut.value = true
            authRepository.logout()
            _isLoggingOut.value = false
        }
    }
}
