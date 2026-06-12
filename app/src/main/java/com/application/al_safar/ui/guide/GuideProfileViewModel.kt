package com.application.al_safar.ui.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.LocalGuide
import com.application.al_safar.domain.model.User
import com.application.al_safar.domain.repository.AuthRepository
import com.application.al_safar.domain.usecase.guide.GetMyTravelersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class GuideProfileUiState {
    object Loading : GuideProfileUiState()
    data class Success(val user: User, val guideDetails: LocalGuide?) : GuideProfileUiState()
    data class Error(val message: String) : GuideProfileUiState()
}

@HiltViewModel
class GuideProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getMyTravelersUseCase: GetMyTravelersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GuideProfileUiState>(GuideProfileUiState.Loading)
    val uiState: StateFlow<GuideProfileUiState> = _uiState

    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut: StateFlow<Boolean> = _isLoggingOut

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            authRepository.getCurrentUser()
                .catch { e ->
                    _uiState.value = GuideProfileUiState.Error(
                        e.localizedMessage ?: "Failed to load profile"
                    )
                }
                .collect { user ->
                    if (user == null) {
                        _uiState.value = GuideProfileUiState.Error("No session found")
                        return@collect
                    }
                    val guideDetails = loadGuideDetails()
                    _uiState.value = GuideProfileUiState.Success(user = user, guideDetails = guideDetails)
                }
        }
    }

    private suspend fun loadGuideDetails(): LocalGuide? =
        getMyTravelersUseCase()
            .getOrNull()
            ?.firstOrNull { it.guide != null }
            ?.guide

    fun logout() {
        viewModelScope.launch {
            _isLoggingOut.value = true
            authRepository.logout()
            _isLoggingOut.value = false
        }
    }
}