package com.application.al_safar.domain.repository

import com.application.al_safar.domain.model.ChatMessage


interface ChatRepository {
    suspend fun sendMessage(
        message: String,
        history: List<ChatMessage>
    ): Result<String>

}