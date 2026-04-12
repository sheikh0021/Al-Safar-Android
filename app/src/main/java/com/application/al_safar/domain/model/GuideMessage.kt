package com.application.al_safar.domain.model

data class GuideMessage(
    val id: Int,
    val senderId: Int,
    val senderName: String,
    val content: String,
    val sentAt: String,
    val isRead: Boolean
)
