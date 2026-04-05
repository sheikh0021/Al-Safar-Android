package com.application.al_safar.data.remote.dto.chat


data class ChatRequestDto(
    val message: String,
    val history: List<ChatHistoryItemDto>
)

data class ChatHistoryItemDto(
    val role: String,
    val content: String
)
