package com.application.al_safar.ui.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.al_safar.domain.model.ChatMessage
import com.application.al_safar.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AiChatbotViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val  _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun sendMessage(userText: String){
        if (userText.isBlank()) return

        viewModelScope.launch {
            //add the message instantly without waiting for the AI
            val userMessage = ChatMessage(role = "user", content = userText)
            _messages.value = _messages.value + userMessage

            _isLoading.value = true
            _error.value = null

            val history = _messages.value.dropLast(1)

            chatRepository.sendMessage(
                message = userText,
                history = history
            ).onSuccess { reply ->
                val aiMessage = ChatMessage(role = "assistant", content = reply)
                _messages.value = _messages.value + aiMessage
            }.onFailure { error ->
                _error.value = error.message ?: "Something went wrong.Please try again."
            }
            _isLoading.value = false
        }
    }
    fun clearError(){
        _error.value = null
    }
}