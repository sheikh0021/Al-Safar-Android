package com.application.al_safar.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.Package
import com.application.al_safar.domain.usecase.booking.GetPackagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPackagesUseCase: GetPackagesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadPackages(null)
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
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val packages: List<Package>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
