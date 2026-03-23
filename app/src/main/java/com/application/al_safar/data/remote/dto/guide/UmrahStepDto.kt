package com.application.al_safar.data.remote.dto.guide

data class UmrahStepDto(
    val id: String,
    val stepNumber: Int,
    val title: String,
    val description: String,
    val arabicText: String?,
    val category: String,
    val estimatedTime: Int?,
    val imageUrl: String?,
    val audioUrl: String?
)
