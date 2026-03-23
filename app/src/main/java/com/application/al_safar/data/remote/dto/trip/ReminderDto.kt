package com.application.al_safar.data.remote.dto.trip


data class ReminderDto(
    val id: String?,
    val title: String,
    val description: String,
    val reminderTime: String,
    val isCompleted: Boolean = false
)
