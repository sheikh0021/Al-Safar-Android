package com.application.al_safar.data.remote.dto.guide_service

data class GuideMessageDto(
    val id: Int,
    val sender_id: Int,
    val sender_name: String,
    val content: String,
    val sent_at: String,
    val is_read: Boolean
)

data class SendMessageRequest(
    val content: String
)
