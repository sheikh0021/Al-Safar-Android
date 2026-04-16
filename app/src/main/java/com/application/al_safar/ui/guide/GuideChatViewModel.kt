package com.application.al_safar.ui.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.GuideMessage
import com.application.al_safar.domain.usecase.guide.GetMessagesUseCase
import com.application.al_safar.domain.usecase.guide.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuideChatViewModel @Inject constructor(
  private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<GuideChatUiState>(GuideChatUiState.Loading)
    val uiState: StateFlow<GuideChatUiState> = _uiState

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending

    fun loadMessages(assignmentId: Int, isGuide: Boolean) {
        viewModelScope.launch {
            _uiState.value = GuideChatUiState.Loading
            getMessagesUseCase(assignmentId, isGuide)
                .onSuccess { messages ->
                    _uiState.value = GuideChatUiState.Success(messages)
                }
                .onFailure { error ->
                    _uiState.value = GuideChatUiState.Error(error.message ?: "Failed to load messages")
                }
        }
    }
    fun sendMessage(assignmentId: Int, content: String, isGuide: Boolean) {
        if (content.isBlank()) return
        viewModelScope.launch {
            _isSending.value = true
            sendMessageUseCase(assignmentId, content, isGuide)
                .onSuccess { newMessage ->
                    val current = (_uiState.value as? GuideChatUiState.Success)?.messages ?: emptyList()
                    _uiState.value = GuideChatUiState.Success(current + newMessage)
                }
                .onFailure { error ->
                    _uiState.value = GuideChatUiState.Error(error.message ?: "Failed to send message")
                }
            _isSending.value = false
        }
    }
}

sealed class GuideChatUiState {
    object Loading : GuideChatUiState()
    data class Success(val messages: List<GuideMessage>): GuideChatUiState()
    data class Error(val message: String): GuideChatUiState()
}