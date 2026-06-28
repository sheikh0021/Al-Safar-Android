package com.application.al_safar.ui.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.GuideAssignment
import com.application.al_safar.domain.usecase.guide.GetMyGuideUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelerMessagesViewModel @Inject constructor(
    private val getMyGuideUseCase: GetMyGuideUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<TravelerMessagesUiState>(TravelerMessagesUiState.Loading)
    val uiState: StateFlow<TravelerMessagesUiState> = _uiState

    init {
        loadConversations()
    }

    fun loadConversations() {
        viewModelScope.launch {
            _uiState.value = TravelerMessagesUiState.Loading
            getMyGuideUseCase().onSuccess { assignments ->
                _uiState.value = TravelerMessagesUiState.Success(assignments)
            }
                .onFailure { error ->
                    _uiState.value = TravelerMessagesUiState.Error(
                        error.message ?: "Failed to load messages"
                    )
                }
        }
    }
    fun getTotalUnreadCount(assignments: List<GuideAssignment>) : Int = assignments.sumOf { it.unreadCount }
}

sealed class TravelerMessagesUiState{
    object Loading : TravelerMessagesUiState()
    data class Success(val assignments : List<GuideAssignment>) : TravelerMessagesUiState()
    data class Error(val message: String) : TravelerMessagesUiState()
}

//separate screens should have separate viewModels - cleaner lifecycle.