package com.application.al_safar.data.remote.api

import com.application.al_safar.data.remote.dto.chat.ChatRequestDto
import com.application.al_safar.data.remote.dto.chat.ChatResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ChatApi {
    @POST("chatbot/message/")
    suspend fun sendMessage(
        @Body request: ChatRequestDto
    ): Response<ChatResponseDto>
}