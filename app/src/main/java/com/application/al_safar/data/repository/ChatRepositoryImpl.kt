package com.application.al_safar.data.repository

import com.application.al_safar.data.remote.api.ChatApi
import com.application.al_safar.data.remote.dto.chat.ChatHistoryItemDto
import com.application.al_safar.data.remote.dto.chat.ChatRequestDto
import com.application.al_safar.domain.model.ChatMessage
import com.application.al_safar.domain.repository.ChatRepository


class ChatRepositoryImpl(
    private val chatApi: ChatApi
): ChatRepository {
    override suspend fun sendMessage(message: String, history: List<ChatMessage>): Result<String> = runCatching {
        val historyDtos = history.map { msg ->
            ChatHistoryItemDto(role = msg.role, content = msg.content)
        }
        val request = ChatRequestDto(
            message = message,
            history = historyDtos
        )
        val response = chatApi.sendMessage(request)

        if (response.isSuccessful) {
            val body = response.body()
            when {
                body?.reply != null -> body.reply

                body?.error != null -> throw Exception(body.error)

                else -> throw Exception("Empty response from AI server.")
            }
        } else {
            throw Exception("Server error: ${response.code()} ${response.message()}")
        }
        
    }

}