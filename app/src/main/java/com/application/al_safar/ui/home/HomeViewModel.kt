package com.application.al_safar.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.Package
import com.application.al_safar.domain.repository.AuthRepository
import com.application.al_safar.domain.usecase.auth.LogoutUseCase
import com.application.al_safar.domain.usecase.booking.GetPackagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPackagesUseCase: GetPackagesUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _travelerName = MutableStateFlow("")
    val travelerName: StateFlow<String> = _travelerName

    private val _logoutComplete = MutableStateFlow(false)
    val logoutComplete: StateFlow<Boolean> = _logoutComplete

    init {
        loadPackages(null)
        loadTravelerName()
    }

    private fun loadTravelerName() {
        viewModelScope.launch {
            authRepository.getCurrentUser()
                .catch { /* keep empty name on error */ }
                .collect { user ->
                    _travelerName.value = user?.let {
                        "${it.firstName} ${it.lastName}".trim().ifBlank { it.firstName }
                    } ?: ""
                }
        }
    }

    fun loadPackages(type: String?) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            getPackagesUseCase(type)
                .onSuccess { packages -> _uiState.value = HomeUiState.Success(packages) }
                .onFailure { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Failed to load packages")
                }
        }
    }

    fun logout(){
        viewModelScope.launch {
            logoutUseCase()
            _logoutComplete.value = true
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val packages: List<Package>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
