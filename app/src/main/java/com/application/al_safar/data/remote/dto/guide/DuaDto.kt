package com.application.al_safar.data.remote.dto.guide


data class DuaDto(
    val id: String,
    val title: String,
    val arabicText: String,
    val transliteration: String?,
    val translation: String?,
    val category: String,
    val audioUrl: String?
)
