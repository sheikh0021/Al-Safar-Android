package com.application.al_safar.ui.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.data.remote.dto.EmergencyContactDto
import com.application.al_safar.domain.repository.EmergencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val emergencyRepository: EmergencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SupportUiState>(SupportUiState.Loading)
    val uiState: StateFlow<SupportUiState> = _uiState

    init {
        loadEmergencyContacts()
    }

    fun loadEmergencyContacts(category: String? = null) {
        viewModelScope.launch {
            _uiState.value = SupportUiState.Loading
            emergencyRepository.getEmergencyContacts(category)
                .onSuccess { contacts -> _uiState.value = SupportUiState.Success(contacts) }
                .onFailure { error ->
                    _uiState.value =
                        SupportUiState.Error(error.message ?: "Failed to load contacts")
                }
        }
    }
}

sealed class SupportUiState {
    object Loading : SupportUiState()
    data class Success(val contacts: List<EmergencyContactDto>) : SupportUiState()
    data class Error(val message: String) : SupportUiState()
}
