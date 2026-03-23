package com.application.al_safar.domain.model


data class UmrahStep(
    val id: String,
    val stepNumber: Int,
    val title: String,
    val description: String,
    val arabicText: String?,
    val isCompleted: Boolean,
    val category: String,
    val estimatedTime: Int?,
    val imageUrl: String?,
    val audioUrl: String?
)
